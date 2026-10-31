#!/usr/bin/env bash
# 卸载 mgit 多仓 Demo：删掉各组件目录里的 .git 与本地 bare 远程仓，把壳工程还原到拆分前状态。
# 用法：scripts/reset-demo.sh --force   （不带 --force 只打印将要执行的删除项）
set -uo pipefail

SHELL_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REMOTE_ROOT="$(dirname "$SHELL_ROOT")/FunViews-remotes"
MODULES=(lib_common module_core module_layout module_components module_animations module_frameworks)
ROLLBACK_REF="backup/pre-mgit-split"

echo "壳工程:   $SHELL_ROOT"
echo "裸仓目录: $REMOTE_ROOT"
echo
echo "将删除的子仓 .git："
for m in "${MODULES[@]}"; do
  [[ -d "$SHELL_ROOT/$m/.git" ]] && echo "  $SHELL_ROOT/$m/.git"
done
echo
echo "将删除的裸仓："
ls -1d "$REMOTE_ROOT"/*.git 2>/dev/null | sed 's/^/  /' || echo "  （无）"
echo
echo "壳工程将回退到 $ROLLBACK_REF（若不存在则保持当前 HEAD）："
git -C "$SHELL_ROOT" rev-parse --short "$ROLLBACK_REF" 2>/dev/null | sed 's/^/  /' || echo "  （回滚点不存在，跳过）"

if [[ "${1:-}" != "--force" ]]; then
  echo
  echo "演练模式：未做任何改动。确认无误后加 --force 执行。"
  exit 0
fi

for m in "${MODULES[@]}"; do
  rm -rf "$SHELL_ROOT/$m/.git"
done

if git -C "$SHELL_ROOT" rev-parse -q --verify "$ROLLBACK_REF" >/dev/null; then
  git -C "$SHELL_ROOT" reset --hard "$ROLLBACK_REF"
  git -C "$SHELL_ROOT" remote remove local 2>/dev/null
  git -C "$SHELL_ROOT" branch --set-upstream-to=origin/mgit_multi_components mgit_multi_components 2>/dev/null
  echo
  echo "壳工程已回退到 $ROLLBACK_REF；组件目录重新由壳工程纳管。"
  echo "确认无误后可自行删除回滚点：git -C $SHELL_ROOT tag -d $ROLLBACK_REF"
else
  echo
  echo "未找到回滚点，仅删除了子仓 .git；组件目录文件仍在磁盘上，需自行处理壳工程的 git rm 记录。"
fi

rm -rf "$REMOTE_ROOT"
echo "裸仓目录已删除。"
