# 説明
JavaとGoでのライブラリの差がないか確認する
Hashのテストを行う際に下記のフォルダに画像ファイル（JPGファイル）を配置すること
./java/server/src/testData.jpg


## 画像ファイルをHash値化した際に
## Goでの実行テスト
/goで実行する

### Hash値の取得の実行
```
$ go run hash/main.go
```

### Xmlで「<>」を出力する処理の実行
```
$ go run xml/main.go
```

## javaでの実行テスト
/javaで実行する  
※Java環境が入っている場合はdockerの処理は飛ばしても問題ありません。

```
$ cd java

$ docker-compose build

$ docker-compose up -d

$ docker container ls
→コンテナ名を取得

$ docker exec -it [コンテナ名] bash 

```

### Hash値の取得の実行

```
$ javac Hash.java
$ java Hash
```

### Xmlで「<>」を出力する処理の実行

```
$ javac DataSet.java
$ javac Xml.java
$ java Xml
```