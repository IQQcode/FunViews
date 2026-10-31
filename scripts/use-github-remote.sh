#!/usr/bin/env bash
# 切换壳工程当前分支的上游，决定 GitSpace 的裸 `git push` 落到哪里：
#   off（默认）→ local/mgit_multi_components：推到本地 bare 镜像，矩阵基准，离线可重复
#   on          → origin/mgit_multi_components：推到 GitHub（origin 为 SSH，非交互可用）
# 用法：bash scripts/use-github-remote.sh on|off|status
set -uo pipefail

SHELL_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BRANCH=mgit_multi_components
LOCAL_UP=local/$BRANCH
GITHUB_UP=origin/$BRANCH

mode=$1
case "$mode" in
  on)  git -C "$SHELL_ROOT" branch --set-upstream-to="$GITHUB_UP" "$BRANCH" >/dev/null ;;
  off) git -C "$SHELL_ROOT" branch --set-upstream-to="$LOCAL_UP" "$BRANCH" >/dev/null ;;
  status) ;;
  *) echo "用法: $0 on|off|status" >&2; exit 2 ;;
esac

up=$(git -C "$SHELL_ROOT" rev-parse --abbrev-ref --symbolic-full-name '@{u}')
ahead=$(git -C "$SHELL_ROOT" rev-list --count "$up..HEAD")
behind=$(git -C "$SHELL_ROOT" rev-list --count "HEAD..$up")
url=$(git -C "$SHELL_ROOT" config --get "remote.${up%%/*}.url")
case "$up" in
  $GITHUB_UP) label="on  → GitHub" ;;
  $LOCAL_UP)  label="off → 本地镜像" ;;
  *)          label="其他" ;;
esac
printf '上游=%s  %s\n目标URL=%s\nahead=%s behind=%s\n' "$up" "$label" "$url" "$ahead" "$behind"

if [[ "$up" == "$GITHUB_UP" ]]; then
  echo "提示：GitHub 模式下 report-states.sh --check 会对 FunViews 报 DRIFT（矩阵基准是 local 镜像），属预期。"
  echo "     测完切回：bash scripts/use-github-remote.sh off"
fi
