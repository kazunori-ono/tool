package main

import (
	"encoding/xml"
	"fmt"
)

type Test struct {
	Data string
}

const testStr = `test<br>"data"'` + "\t\r\n& "

func main() {
	testData := &Test{testStr}

	xmlOnix, err := xml.MarshalIndent(&testData, "", "    ")
	if err != nil {
		fmt.Errorf("%v", err)
		return
	}
	fmt.Println(string(xmlOnix))
}
