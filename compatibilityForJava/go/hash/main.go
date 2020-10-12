package main

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"io/ioutil"
)

const ImagePath = "../java/server/src/testData.jpg"

func main() {
	file, _ := ioutil.ReadFile(ImagePath)
	hash := sha256.Sum256(file)
	fmt.Println(hex.EncodeToString(hash[:]))
}
