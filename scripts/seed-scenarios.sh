#!/usr/bin/env bash
# 重铺 FunViews mgit 多仓 Demo 的全场景矩阵到初始态（幂等，可反复执行）。
# 依赖：各组件目录的 origin 与壳工程的 local 指向 ../FunViews-remotes/*.git，
#       且每个裸仓内记录了基线 refs/baseline/main（见 docs/MULTI-REPO-DEMO.md 第 2 节）。
#
# 确定性：所有场景提交都钉死 author/committer 日期，因此每次重铺产出完全相同的 commit hash，
#         远程（含 GitHub origin）不会被重铺甩成分叉；改日期或改 fixture 才会产生新 hash。
# 用法：bash scripts/seed-scenarios.sh
set -uo pipefail

SHELL_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
RR="$(dirname "$SHELL_ROOT")/FunViews-remotes"
SEEDS="$SHELL_ROOT/scripts/seeds"

die() { echo "seed 失败: $*" >&2; exit 1; }

baseline() { git --git-dir="$RR/$1.git" rev-parse refs/baseline/main; }

# 仓库、工作区、远程 main 三者一起回到基线
reset_repo() {
  local repo="$1" name="$2" branch="$3" remote="$4" base
  base=$(baseline "$name") || die "$name 缺少 refs/baseline/main"
  cd "$repo" || die "$repo 不存在"
  git checkout -q -B "$branch" "$base" || die "$name 回退基线失败"
  git push -q -f "$remote" "$base:refs/heads/main" || die "$name 远程回退失败"
  git update-ref "refs/remotes/$remote/main" "$base"
}

# 只提交指定路径，并钉死时间戳，避免把无关文件扫进场景提交
commit_d() {
  local msg="$1" date="$2"; shift 2
  git add -- "$@" || die "git add 失败: $*"
  GIT_AUTHOR_DATE="$date" GIT_COMMITTER_DATE="$date" git commit -q -m "$msg" || die "git commit 失败: $msg"
}

# 在裸仓 main 上追加一笔「他人提交」
seed_remote_commit() {
  local name="$1" src="$2" dest="$3" msg="$4" author="$5" email="$6" date="$7" tmp
  tmp=$(mktemp -d) || die "mktemp 失败"
  git clone -q "$RR/$name.git" "$tmp" || die "$name 临时克隆失败"
  cd "$tmp" || die
  mkdir -p "$(dirname "$dest")"
  cp "$src" "$dest" || die "拷贝 fixture 失败: $src"
  git add -- "$dest"
  GIT_AUTHOR_DATE="$date" GIT_COMMITTER_DATE="$date" \
    git -c user.name="$author" -c user.email="$email" commit -q \
      --author="$author <$email>" -m "$msg" || die "$name 他人提交失败"
  git push -q origin main || die "$name 推送他人提交失败"
  cd "$SHELL_ROOT" || die
  rm -rf "$tmp"
}

echo "[1/7] 壳工程 FunViews：2 个未推送提交（Story-2001 壳侧装配）"
cd "$SHELL_ROOT" || die
SHELL_BASE=$(baseline FunViews) || die "壳工程缺少 refs/baseline/main"
git checkout -q -B mgit_multi_components "$SHELL_BASE" || die "壳工程回退基线失败"
git push -q -f local "$SHELL_BASE:refs/heads/mgit_multi_components" || die "壳工程远程回退失败"
git branch --set-upstream-to=local/mgit_multi_components mgit_multi_components >/dev/null
mkdir -p app/src/main/java/top/iqqcode/funviews/ui app/src/main/res/layout
cp "$SEEDS/app/CartSummaryFragment.kt" app/src/main/java/top/iqqcode/funviews/ui/ || die
cp "$SEEDS/app/fragment_cart_summary.xml" app/src/main/res/layout/ || die
commit_d "feat(cart): 结算汇总页 CartSummaryFragment 与布局（Story-2001）" "2026-01-14T16:10:00+08:00" \
  app/src/main/java/top/iqqcode/funviews/ui/CartSummaryFragment.kt \
  app/src/main/res/layout/fragment_cart_summary.xml
grep -q 'name="cart_summary"' app/src/main/res/values/arrays.xml ||
  perl -0pi -e '
    s{(        <item name="less_level1_item">分类</item>\n)}{$1        <item name="cart_summary">购物车结算</item>\n};
    s{(        <item name="less_level1_item">"com\.kunminx\.linkagelistview\.ui\.LessPrimaryItemSampleFragment"</item>\n)}{$1        <item name="cart_summary">"top.iqqcode.funviews.ui.CartSummaryFragment"</item>\n};
  ' app/src/main/res/values/arrays.xml || die "arrays.xml 注册失败"
commit_d "feat(cart): 首页目录注册购物车结算入口（Story-2001）" "2026-01-14T17:05:00+08:00" \
  app/src/main/res/values/arrays.xml

echo "[2/7] lib_common：同步基线（Story-2001 契约已推送）"
reset_repo "$SHELL_ROOT/lib_common" lib_common main origin
mkdir -p src/main/java/top/iqqcode/lib/common/router src/main/java/top/iqqcode/lib/common/bean
cp "$SEEDS/lib_common/CartRouter.kt" src/main/java/top/iqqcode/lib/common/router/ || die
cp "$SEEDS/lib_common/CartItem.kt" src/main/java/top/iqqcode/lib/common/bean/ || die
commit_d "feat: 新增购物车路由契约与 CartItem 数据模型（Story-2001）" "2026-01-09T10:00:00+08:00" \
  src/main/java/top/iqqcode/lib/common/router/CartRouter.kt \
  src/main/java/top/iqqcode/lib/common/bean/CartItem.kt
git push -q origin main || die "lib_common 推送失败"

echo "[3/7] module_core：ahead 2（待批量 Push）"
reset_repo "$SHELL_ROOT/module_core" module_core main origin
mkdir -p src/main/java/top/iqqcode/module/core/cart
cp "$SEEDS/module_core/CartRepository.kt" src/main/java/top/iqqcode/module/core/cart/ || die
commit_d "feat(cart): 购物车数据源 CartRepository（Story-2001）" "2026-01-10T11:20:00+08:00" \
  src/main/java/top/iqqcode/module/core/cart/CartRepository.kt
cp "$SEEDS/module_core/CheckoutCalculator.kt" src/main/java/top/iqqcode/module/core/cart/ || die
commit_d "feat(cart): 结算价计算 CheckoutCalculator（Story-2001）" "2026-01-10T15:40:00+08:00" \
  src/main/java/top/iqqcode/module/core/cart/CheckoutCalculator.kt

echo "[4/7] module_layout：behind 2（远程两笔他人提交，待批量 Pull）"
reset_repo "$SHELL_ROOT/module_layout" module_layout main origin
cd "$SHELL_ROOT" || die
seed_remote_commit module_layout "$SEEDS/module_layout/CartFlowHelper.kt" \
  "src/main/java/top/iqqcode/module/layout/page/CartFlowHelper.kt" \
  "feat(layout): 购物车吸顶与滑动列宽辅助（Story-2001）" \
  zhang-san zhangsan@example.com "2026-01-12T10:24:00+08:00"
seed_remote_commit module_layout "$SEEDS/module_layout/CartFlowItemContainer.kt" \
  "src/main/java/top/iqqcode/module/layout/page/CartFlowItemContainer.kt" \
  "feat(layout): 滑动删除条目容器 CartFlowItemContainer（Story-2001）" \
  zhang-san zhangsan@example.com "2026-01-13T09:05:00+08:00"
cd "$SHELL_ROOT/module_layout" || die
git checkout -q main && git fetch -q origin || die "module_layout fetch 失败"

echo "[5/7] module_components：分叉（ahead 1 / behind 1）"
cd "$SHELL_ROOT" || die
reset_repo "$SHELL_ROOT/module_components" module_components main origin
mkdir -p src/main/java/top/iqqcode/module/components/cart
cp "$SEEDS/module_components/CartBadgeView.kt" src/main/java/top/iqqcode/module/components/cart/ || die
commit_d "feat(components): 购物车角标 CartBadgeView（Story-2001）" "2026-01-13T14:00:00+08:00" \
  src/main/java/top/iqqcode/module/components/cart/CartBadgeView.kt
cd "$SHELL_ROOT" || die
seed_remote_commit module_components "$SEEDS/module_components/CartPriceTextView.kt" \
  "src/main/java/top/iqqcode/module/components/cart/CartPriceTextView.kt" \
  "feat(components): 价格文案控件 CartPriceTextView（Story-2001）" \
  li-si lisi@example.com "2026-01-13T14:30:00+08:00"
cd "$SHELL_ROOT/module_components" || die
git checkout -q main && git fetch -q origin || die "module_components fetch 失败"

echo "[6/7] module_animations：脏工作区（已暂存 + 未暂存 + 未跟踪）"
cd "$SHELL_ROOT" || die
reset_repo "$SHELL_ROOT/module_animations" module_animations main origin
rm -rf src/main/res/anim
mkdir -p src/main/java/top/iqqcode/module/animations/animator src/main/res/anim
cp "$SEEDS/module_animations/CartBounceAnimator.kt" src/main/java/top/iqqcode/module/animations/animator/ || die
git add -- src/main/java/top/iqqcode/module/animations/animator/CartBounceAnimator.kt
printf '\n// 已暂存之后新增的未暂存改动：弹跳时长待与动画规范对齐\n' \
  >> src/main/java/top/iqqcode/module/animations/animator/CartBounceAnimator.kt
cp "$SEEDS/module_animations/anim_cart_bounce.xml" src/main/res/anim/ || die
grep -q 'cart_highlight' src/main/res/values/colors.xml ||
  perl -0pi -e 's{\n</resources>}{\n\n    <color name="cart_highlight">#FFFF6B35</color>\n</resources>}' \
    src/main/res/values/colors.xml

echo "[7/7] module_frameworks：未推送特性分支 + 未推送 tag + 待删本地分支"
cd "$SHELL_ROOT" || die
reset_repo "$SHELL_ROOT/module_frameworks" module_frameworks main origin
git tag -d v1.2.0-story-2001 >/dev/null 2>&1
git branch -q -D feature/story-2001-renderer 2>/dev/null
git branch -q -D release/1.1.0 2>/dev/null
# 上一轮测试若把特性分支/Tag 推上去了，这里一并从裸仓删掉，保证「新分支首推」可重复
git push -q origin :refs/heads/feature/story-2001-renderer 2>/dev/null
git push -q origin :refs/tags/v1.2.0-story-2001 2>/dev/null
git fetch -q --prune origin
git checkout -q -b feature/story-2001-renderer || die "特性分支创建失败"
mkdir -p src/main/java/top/iqqcode/module/frameworks/checkout
cp "$SEEDS/module_frameworks/CheckoutRenderHandler.kt" src/main/java/top/iqqcode/module/frameworks/checkout/ || die
commit_d "feat(checkout): 结算渲染 Handler（Story-2001）" "2026-01-14T09:30:00+08:00" \
  src/main/java/top/iqqcode/module/frameworks/checkout/CheckoutRenderHandler.kt
GIT_COMMITTER_DATE="2026-01-14T09:30:00+08:00" \
  git tag -a v1.2.0-story-2001 -m "Story-2001 结算链路联调版本" || die "tag 创建失败"
git branch release/1.1.0 main

cd "$SHELL_ROOT" || die
echo
bash "$SHELL_ROOT/scripts/report-states.sh"
