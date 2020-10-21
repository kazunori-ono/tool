package main

import (
	"bytes"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"golang.org/x/image/draw"
	"image"
	"image/color"
	"image/jpeg"
	"io/ioutil"
	"os"
)

const ImagePath = "../java/server/src/testData.jpg"
const OutputImagePath = "../java/server/src/testData2_go.jpg"

func main() {
	fmt.Println("Start")

	if err := exe(); err != nil {
		fmt.Println("Error", err.Error())
		return
	}
	fmt.Println("End")
}

func exe() error {
	file, _ := ioutil.ReadFile(ImagePath)

	data, err := resizeThumbnail(file)
	if err != nil {
		return err
	}
	hash := sha256.Sum256(data)
	fmt.Println(hex.EncodeToString(hash[:]))
	return nil
}

func resizeThumbnail(data []byte) ([]byte, error) {
	imgSrc, _, err := image.Decode(bytes.NewReader(data))
	if err != nil {
		return nil, err
	}

	rctSrc := imgSrc.Bounds()
	width := rctSrc.Dx()
	height := rctSrc.Dy()

	var point image.Point
	if width*4 != height*3 {
		if width*4 > height*3 {
			//縦幅を補完
			height = (width * 4) / 3
			point = image.Point{0, -(height - rctSrc.Dy()) / 2}
		} else {
			//横幅を補完
			width = (height * 3) / 4
			point = image.Point{-(width - rctSrc.Dx()) / 2, 0}
		}
	}

	imgDst := image.NewRGBA(image.Rect(0, 0, width, height))
	draw.Draw(imgDst, imgDst.Bounds(), &image.Uniform{color.White}, image.ZP, draw.Src)
	draw.Draw(imgDst, imgDst.Bounds(), imgSrc, point, draw.Over)

	out, err := os.Create(OutputImagePath)
	if err != nil {
		return nil, err
	}

	imageBuf := &bytes.Buffer{}
	if err := jpeg.Encode(out, imgDst, &jpeg.Options{Quality: 100}); err != nil {
		return nil, err
	}

	return imageBuf.Bytes(), nil
}
