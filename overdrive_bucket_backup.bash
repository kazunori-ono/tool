#!/bin/bash -x

# 実行時ディレクトリ（htmlが保存されるパス）
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# 実行コマンド
# bash overdrive_bucket_backup.bash [コマンド cp/rm] [環境変数名(ENV)]
BUCKET_OUTPUT="output"
BUCKET_IMG="img"
BUCKET_HISTORY="history"
BUCKET_BISAC="bisac"

if [ -z $1 ]; then
    echo "コマンドを指定してください　例）cp"
    exit 1
elif [ ! "cp" = $1 ] && [ ! "rm" = $1 ]; then
    echo "コマンドを指定が間違っています　cp/rm のみ"
    exit 1
elif [ -z $2 ]; then
    echo "環境変数名を指定してください　例）dev"
    exit 1
fi

BUCKET=dist.$2.overdrive

# バックアップ処理
if [ $1 = "cp" ]; then
    echo "cp start $BUCKET"
    aws s3 cp s3://$BUCKET/$BUCKET_OUTPUT/ s3://$BUCKET/$BUCKET_OUTPUT.org/ --recursive
    aws s3 cp s3://$BUCKET/$BUCKET_IMG/ s3://$BUCKET/$BUCKET_IMG.org/ --recursive
    aws s3 cp s3://$BUCKET/$BUCKET_HISTORY/ s3://$BUCKET/$BUCKET_HISTORY.org/ --recursive
    aws s3 cp s3://$BUCKET/$BUCKET_BISAC/ s3://$BUCKET/$BUCKET_BISAC.org/ --recursive
    echo "cp end"
    
elif [ $1 = "rm" ]; then
    echo "rm start $BUCKET"
    aws s3 rm s3://${BUCKET}/${BUCKET_OUTPUT}.org/ --recursive
    aws s3 rm s3://${BUCKET}/${BUCKET_IMG}.org/ --recursive
    aws s3 rm s3://${BUCKET}/${BUCKET_HISTORY}.org/ --recursive
    aws s3 rm s3://${BUCKET}/${BUCKET_BISAC}.org/ --recursive
    echo "rm end"
fi

aws s3 ls s3://${BUCKET}/