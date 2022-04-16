# Simple Captcha Solver API

A simple captcha solver API service

This project is released and distributed under the [Apache License, v2.0](http://www.apache.org/licenses/LICENSE-2.0)

## Features

- Supports JPG, PNG, and BMP captcha
- Provides API for Base64 Data URL image (and only Base64 support for now)
- Customizable CV/OCR parameters

## Required Software

- Java 8 or newer

This project uses CV library (OpenCV, Tesseract, and Leptonica) wrappers
from [JavaCV](https://github.com/bytedeco/javacv)

## Usage

To start API service, run following command:

```shell
java -jar simple-captcha-solver-api.jar --spring.config.location=<path_to_application.yml>
```

## API

### `POST /solveBase64Captcha`

Request includes a Base64 Data URL of JPG, PNG or BMP captcha image:

```json
{
  "base64": "data:image/jpeg;base64,xxx..."
}
```

Response includes status of OCR process (successful or not), solved captcha result, and confidence of result:

```json
{
  "success": true,
  "text": "1234",
  "confidence": 93
}
```

## Customization

An example of application.yml:

```yaml
server:
  port: 9876

custom-configuration:
  api:
    length_expected: 4
    fail_if_length_unexpected: false
    minimum_confidence: 90
    fail_if_unconfident: false
    request_size_limit_bytes: 0
  ocr:
    threshold_thresh: 130
    threshold_maxval: 255
    erode_element_size: 2
    resize_size: 3
    tess_data: ./tessdata # required, modify to suit your case
    tess_lang: eng # required, modify to suit your case
    tess_psm: 7
```

The Processors will perform image OCR by following steps:

1. Convert image to grayscale (opencv_imgproc.cvtColor)
2. Convert grayscale image to binary image (opencv_imgproc.threshold)*
3. Erode binary image (opencv_imgproc.erode)*
4. Resize eroded image (opencv_imgproc.erode)*
5. Perform OCR (TessBaseAPI)*

Steps marked with `*` provides customizable parameters

### API

|config|required|default|note|
|---------------------------|---|-----|-|
|length_expected            |   |4    |int, expected length of solved captcha string|
|fail_if_length_unexpected  |   |false|if set to true, `success` will be false if solved captcha result length does not match `length_expected`<br>if set to false, solved captcha result length check will be skipped and `length_expected` will be ignored|
|minimum_confidence         |   |90   |int, minimum acceptable confidence of OCR result|
|fail_if_unconfident        |   |false|if set to true, `success` will be false if confidence of solved captcha is less than `minimum_confidence`<br>if set to false, solved captcha confidence check will be skipped and `minimum_confidence` will be ignored|
|request_size_limit_bytes   |   |0    |int, requests larger than `request_size_limit_bytes` will be discarded<br>0 means allow requests of any size|

### OCR

|config|required|default|note|
|-------------------|---|---|-|
|threshold_thresh   |   |130|double, `thresh` for cv::threshold|
|threshold_maxval   |   |255|double, `maxval` for cv::threshold|
|erode_element_size |   |2  |int, size of structuring element used for cv::erode|
|resize_size        |   |3  |int, length/width of resized image will be length/width * `resize_size`|
|tess_data          |yes|-  |Tesseract `--tessdata-dir`|
|tess_lang          |yes|-  |Tesseract `-l LANG`|
|tess_psm           |   |7  |Tesseract `--psm`|

## Build

The following example builds a jar for x64 windows and linux platform:

```shell
mvn package -D"javacpp.platform.custom" -D"javacpp.platform.host" -D"javacpp.platform.windows-x86_64" -D"javacpp.platform.linux-x86_64"
```

To build for particular platform(s),
see [here](https://github.com/bytedeco/javacpp-presets/wiki/Reducing-the-Number-of-Dependencies)