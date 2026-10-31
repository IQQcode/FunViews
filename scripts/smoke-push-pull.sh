#!/usr/bin/env bash
# 冒烟跑通 FunViews mgit 多仓 Demo 的全场景矩阵：
# 前置校验 → Fetch → 批量 Push → 批量 Pull → 分叉 Pull → 逐仓提交 → 新分支首推/Tag → 同步仓跳过
# → 错误路径观测 → 自动重铺回初始态。
# 每一步都对结果做断言，用于把 CLI 等价基线固化下来（GitSpace UI 侧只需与这张结果表对齐）。
#
# 用法：
#   bash scripts/smoke-push-pull.sh            跑完并自动重铺
#   bash scripts/smoke-push-pull.sh --keep     跑完保留消耗后的状态（便于手工接着验证）
#   bash scripts/smoke-push-pull.sh --force    跳过「初始态必须符合矩阵」的前置断言
set -uo pipefail

SHELL_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MODULES="lib_common module_core module_layout module_components module_animations module_frameworks"

KEEP=0
FORCE=0
for arg in "$@"; do
  case "$arg" in
    --keep)  KEEP=1 ;;
    --force) FORCE=1 ;;
    *) echo "未知参数: $arg（支持 --keep / --force）" >&2; exit 2 ;;
  esac
done

PASS_COUNT=0
FAIL_COUNT=0

check() {
  local name="$1" expect="$2" actual="$3"
  if [[ "$expect" == "$actual" ]]; then
    printf '  \033[32mPASS\033[0m %-42s %s\n' "$name" "$actual"
    PASS_COUNT=$((PASS_COUNT + 1))
  else
    printf '  \033[31mFAIL\033[0m %-42s expect=%s actual=%s\n' "$name" "$expect" "$actual"
    FAIL_COUNT=$((FAIL_COUNT + 1))
  fi
}

observe() {
  printf '  OBSERVE %-40s %s\n' "$1" "$2"
}

ahead()  { git -C "$1" rev-list --count '@{u}..HEAD' 2>/dev/null || echo NO_UPSTREAM; }
behind() { git -C "$1" rev-list --count 'HEAD..@{u}' 2>/dev/null || echo NO_UPSTREAM; }
dirty()  { git -C "$1" status --porcelain | grep -c . || true; }
head_of() { git -C "$1" rev-parse HEAD; }

echo "== 0. 前置校验：仓库必须处于文档 §3 的矩阵初始态 =="
if bash "$SHELL_ROOT/scripts/report-states.sh" --check >/dev/null 2>&1; then
  check "初始态符合矩阵" ok ok
else
  if [[ $FORCE -eq 1 ]]; then
    observe "初始态偏离矩阵" "已用 --force 跳过断言，结果不作数"
  else
    echo "  初始态偏离矩阵。先执行 bash scripts/seed-scenarios.sh 重铺后重试。"
    bash "$SHELL_ROOT/scripts/report-states.sh" --check 2>/dev/null | sed -n '/比对/,$p'
    exit 1
  fi
fi

echo
echo "== 1. 全部仓库 Fetch =="
git -C "$SHELL_ROOT" fetch -q --prune local
for m in $MODULES; do git -C "$SHELL_ROOT/$m" fetch -q --prune origin; done
check "Fetch 不改变任何仓的 ahead/behind" "2/2" "$(ahead "$SHELL_ROOT/module_core")/$(behind "$SHELL_ROOT/module_layout")"
check "Fetch 后 module_components 仍分叉" "1/1" "$(ahead "$SHELL_ROOT/module_components")/$(behind "$SHELL_ROOT/module_components")"

echo
echo "== 2. 批量 Push：未推送仓 = 壳工程 + module_core =="
git -C "$SHELL_ROOT/module_core" push -q origin main
git -C "$SHELL_ROOT" push -q local mgit_multi_components
check "module_core 推送后 ahead" 0 "$(ahead "$SHELL_ROOT/module_core")"
check "壳工程推送后 ahead" 0 "$(ahead "$SHELL_ROOT")"

echo
echo "== 3. 批量 Pull：落后仓 = module_layout（fast-forward）=="
git -C "$SHELL_ROOT/module_layout" pull -q --ff-only origin main
check "module_layout 拉取后 ahead/behind" "0/0" "$(ahead "$SHELL_ROOT/module_layout")/$(behind "$SHELL_ROOT/module_layout")"
check "远程他人提交内容已落到工作区" present \
  "$(test -f "$SHELL_ROOT/module_layout/src/main/java/top/iqqcode/module/layout/page/CartFlowHelper.kt" && echo present || echo missing)"
check "拉取后无 merge 提交（纯 FF）" 0 \
  "$(git -C "$SHELL_ROOT/module_layout" rev-list --merges '@{u}..HEAD' | grep -c . || true)"

echo
echo "== 4. 分叉仓 Pull：module_components（merge 口径）=="
git -C "$SHELL_ROOT/module_components" -c core.editor=true pull -q --no-rebase --no-edit origin main
check "module_components 拉取后 ahead/behind" "2/0" "$(ahead "$SHELL_ROOT/module_components")/$(behind "$SHELL_ROOT/module_components")"
check "merge 产生 1 条合并提交" 1 \
  "$(git -C "$SHELL_ROOT/module_components" rev-list --merges '@{u}..HEAD' | grep -c . || true)"
check "两侧改动并存" both \
  "$(test -f "$SHELL_ROOT/module_components/src/main/java/top/iqqcode/module/components/cart/CartBadgeView.kt" && test -f "$SHELL_ROOT/module_components/src/main/java/top/iqqcode/module/components/cart/CartPriceTextView.kt" && echo both || echo missing)"

echo
echo "== 5. 逐仓提交：module_animations 脏工作区 =="
before=$(dirty "$SHELL_ROOT/module_animations")
git -C "$SHELL_ROOT/module_animations" add -A
git -C "$SHELL_ROOT/module_animations" commit -q -m "feat(anim): 加购弹跳动画与高亮色（Story-2001）"
git -C "$SHELL_ROOT/module_animations" push -q origin main
check "提交前工作区脏项数" 3 "$before"
check "提交后脏项数" 0 "$(dirty "$SHELL_ROOT/module_animations")"
check "提交并推送后 ahead" 0 "$(ahead "$SHELL_ROOT/module_animations")"

echo
echo "== 6. 新分支首推 + Tag：module_frameworks =="
git -C "$SHELL_ROOT/module_frameworks" push -q -u origin feature/story-2001-renderer
git -C "$SHELL_ROOT/module_frameworks" push -q origin v1.2.0-story-2001
check "特性分支 upstream" origin/feature/story-2001-renderer \
  "$(git -C "$SHELL_ROOT/module_frameworks" rev-parse --abbrev-ref '@{u}' 2>/dev/null || echo none)"
check "远程 tag 可见" v1.2.0-story-2001 \
  "$(git -C "$SHELL_ROOT/module_frameworks" ls-remote --tags origin | grep -o 'v1\.2\.0-story-2001' | head -1)"

echo
echo "== 7. 已同步仓跳过：lib_common =="
lib_head=$(head_of "$SHELL_ROOT/lib_common")
git -C "$SHELL_ROOT/lib_common" push -q origin main 2>/dev/null
check "无待推送内容，HEAD 不变" same "$( [[ "$lib_head" == "$(head_of "$SHELL_ROOT/lib_common")" ]] && echo same || echo changed)"

echo
echo "== 8. 远程推送观测：壳工程 → GitHub origin（协议取决于 remote 配置）=="
origin_url=$(git -C "$SHELL_ROOT" remote get-url origin)
gh_out=$(GIT_TERMINAL_PROMPT=0 git -C "$SHELL_ROOT" push --dry-run origin mgit_multi_components 2>&1 || true)
gh_last=$(grep -v '^[[:space:]]*$' <<<"$gh_out" | tail -1)
if grep -qE 'unable to access|Empty reply|Could not resolve|could not read Username|Permission denied|Host key verification' <<<"$gh_out"; then
  observe "origin 不可用（$origin_url）" "$gh_last"
  observe "批量结果面板期望" "该仓标记为失败并展示原始错误文本，不中断其余仓库"
else
  observe "origin 可用（$origin_url）" "$gh_last"
  observe "批量结果面板期望" "该仓推送成功；要验错误路径请临时把 origin 切回 HTTPS（见文档 §8）"
fi

echo
if [[ $KEEP -eq 1 ]]; then
  echo "== --keep：保留消耗后的状态，跳过重铺 =="
  bash "$SHELL_ROOT/scripts/report-states.sh" | sed -n '1,9p'
  echo
  echo "下次跑前需先 bash scripts/seed-scenarios.sh"
else
  echo "== 9. 自动重铺回矩阵初始态 =="
  bash "$SHELL_ROOT/scripts/seed-scenarios.sh" >/dev/null 2>&1
  check "重铺后对账" ok \
    "$(bash "$SHELL_ROOT/scripts/report-states.sh" --check >/dev/null 2>&1 && echo ok || echo drift)"
  for r in $MODULES; do
    observe "$r 远程分支" "$(git --git-dir="$SHELL_ROOT/../FunViews-remotes/$r.git" for-each-ref --format='%(refname:short)' refs/heads | tr '\n' ' ')"
  done
fi

echo
echo "结果：PASS=$PASS_COUNT FAIL=$FAIL_COUNT"
[[ $FAIL_COUNT -eq 0 ]] || exit 1
