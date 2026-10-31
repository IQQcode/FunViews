#!/usr/bin/env bash
# 打印（或校验）FunViews mgit 多仓 Demo 的每个仓库状态，供 GitSpace 推送/拉取自动化测试对账。
# 用法：
#   scripts/report-states.sh          只打印状态表
#   scripts/report-states.sh --check  与 EXPECT 矩阵比对，漂移时退出码 1
set -uo pipefail

SHELL_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REMOTE_ROOT="$(dirname "$SHELL_ROOT")/FunViews-remotes"

REPOS=(
  "$SHELL_ROOT"
  "$SHELL_ROOT/lib_common"
  "$SHELL_ROOT/module_core"
  "$SHELL_ROOT/module_layout"
  "$SHELL_ROOT/module_components"
  "$SHELL_ROOT/module_animations"
  "$SHELL_ROOT/module_frameworks"
)

# 期望矩阵：路径|分支|upstream|ahead|behind|staged|unstaged|untracked|本地tag数
EXPECT=(
  "$SHELL_ROOT|mgit_multi_components|local/mgit_multi_components|2|0|0|0|0|1"
  "$SHELL_ROOT/lib_common|main|origin/main|0|0|0|0|0|0"
  "$SHELL_ROOT/module_core|main|origin/main|2|0|0|0|0|0"
  "$SHELL_ROOT/module_layout|main|origin/main|0|2|0|0|0|0"
  "$SHELL_ROOT/module_components|main|origin/main|1|1|0|0|0|0"
  "$SHELL_ROOT/module_animations|main|origin/main|0|0|1|2|1|0"
  "$SHELL_ROOT/module_frameworks|feature/story-2001-renderer|NO_UPSTREAM|-|-|0|0|0|1"
)

measure() {
  local repo="$1" branch upstream ahead behind staged unstaged untracked tags
  branch=$(git -C "$repo" rev-parse --abbrev-ref HEAD)
  upstream=$(git -C "$repo" rev-parse --abbrev-ref --symbolic-full-name '@{u}' 2>/dev/null || echo NO_UPSTREAM)
  if [[ "$upstream" == NO_UPSTREAM ]]; then
    ahead='-'; behind='-'
  else
    ahead=$(git -C "$repo" rev-list --count "$upstream..HEAD")
    behind=$(git -C "$repo" rev-list --count "HEAD..$upstream")
  fi
  staged=$(git -C "$repo" diff --cached --name-only | grep -c . || true)
  unstaged=$(git -C "$repo" diff --name-only | grep -c . || true)
  untracked=$(git -C "$repo" ls-files --others --exclude-standard | sed -n 's#.*#x#p' | wc -l | tr -d ' ')
  tags=$(git -C "$repo" tag -l | grep -c . || true)
  local unpushed_tags
  unpushed_tags=$(git -C "$repo" tag --no-merged 2>/dev/null | wc -l | tr -d ' ')
  printf '%s|%s|%s|%s|%s|%s|%s|%s|%s|%s\n' \
    "$(basename "$repo")" "$branch" "$upstream" "$ahead" "$behind" \
    "$staged" "$unstaged" "$untracked" "$tags" "$unpushed_tags"
}

printf '%-22s %-28s %-28s %6s %7s %7s %9s %10s %5s\n' \
  REPO BRANCH UPSTREAM AHEAD BEHIND STAGED UNSTAGED UNTRACKED TAGS
for repo in "${REPOS[@]}"; do
  IFS='|' read -r name branch upstream ahead behind staged unstaged untracked tags _ < <(measure "$repo")
  printf '%-22s %-28s %-28s %6s %7s %7s %9s %10s %5s\n' \
    "$name" "$branch" "$upstream" "$ahead" "$behind" "$staged" "$unstaged" "$untracked" "$tags"
done

echo
echo "远程仓（本地 bare）：$REMOTE_ROOT"
ls -1 "$REMOTE_ROOT" 2>/dev/null | sed 's/^/  /'

if [[ "${1:-}" != "--check" ]]; then
  exit 0
fi

echo
echo "=== 与期望矩阵比对 ==="
fail=0
for want in "${EXPECT[@]}"; do
  IFS='|' read -r wpath wbranch wupstream wahead wbehind wstaged wunstaged wuntracked wtags <<< "$want"
  actual_line=$(measure "$wpath")
  IFS='|' read -r aname abranch aupstream aahead abehind astaged aunstaged auntracked atags _ <<< "$actual_line"
  if [[ "$abranch" == "$wbranch" && "$aupstream" == "$wupstream" && "$aahead" == "$wahead" \
        && "$abehind" == "$wbehind" && "$astaged" == "$wstaged" && "$aunstaged" == "$wunstaged" \
        && "$auntracked" == "$wuntracked" && "$atags" == "$wtags" ]]; then
    echo "  OK   $aname"
  else
    echo "  DRIFT $aname"
    echo "        expect: branch=$wbranch upstream=$wupstream ahead=$wahead behind=$wbehind staged=$wstaged unstaged=$wunstaged untracked=$wuntracked tags=$wtags"
    echo "        actual: branch=$abranch upstream=$aupstream ahead=$aahead behind=$abehind staged=$astaged unstaged=$aunstaged untracked=$auntracked tags=$atags"
    fail=1
  fi
done

if [[ $fail -eq 0 ]]; then
  echo "场景矩阵一致。"
else
  echo "存在漂移（执行过 Push/Pull/提交后会正常出现，重跑见 docs/MULTI-REPO-DEMO.md）。"
fi
exit $fail
