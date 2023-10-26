# 사과 당도 측정 안드로이드 앱 ‘달당’
SW마에스트로 10기, 농림축산식품 공공 및 빅데이터 활용 창업경진대회 최우수상

사과의 받침 사진으로 색상, 받침의 형태를 분석하여 당도를 측정해주는 서비스

## 프로젝트 정보

### 프로젝트 기간
2019.04 ~ 2020.08

### 업무 분담
안드로이드 1인, 백엔드 1인, AI 1인

안드로이드 개발 담당

### 기술 스택
Android, Kotlin, Retrofit2, Gson, MPAndroidChart, Fotoapparat

## 앱 스크린샷
<img src="https://user-images.githubusercontent.com/15646373/191761916-0fd5ba3d-85a8-4db0-acf8-e74522fe1402.jpg" width="200"/> <img src="https://user-images.githubusercontent.com/15646373/191761896-87502d6d-1e32-467a-96ea-674c92fc6d96.jpg" width="200"/> <img src="https://user-images.githubusercontent.com/15646373/191761910-d06675d5-2c38-487a-b562-9859de58bf41.jpg" width="200"/>

<img src="https://user-images.githubusercontent.com/15646373/191761921-f10dff68-5dbc-452e-a55f-a38c69cb2ec6.jpg" width="200"/> <img src="https://user-images.githubusercontent.com/15646373/191761925-79bd4dae-ec1b-4bb4-ba45-c5d706a074c1.jpg" width="200"/> <img src="https://user-images.githubusercontent.com/15646373/191761929-3cc656cb-2fb0-4dcd-a9a7-ce84637c80fc.jpg" width="200"/>

## 개발 내용
### 당도 측정 기능
클라이언트에서 사과 받침 사진을 촬영하여 서버에 전송하면 서버에서 색상과 받침의 형태를 분석하여 당도 정보를 Response로 넘겨줍니다.
이 때 Fotoapparat 라이브러리를 사용하여 사진을 촬영한 후, 결과물을 OutputStream을 통해 jpg 파일로 저장했고 Multipart로 서버에 전송했습니다.
그리고 받아온 Response를 토대로 측정 결과를 띄웠습니다.

### 그래프 그리기
<img width="393" alt="image" src="https://github.com/hyemdooly/DaldangAndroid/assets/15646373/f3768859-7571-4f4f-a775-a6a0247f090c">

MPAndroidChart 라이브러리를 사용하여 그래프를 그리는 중, 커스텀하지 않으면 디자인대로 Marker를 그릴 수 없었습니다.
따라서 해상도별로 배경 이미지를 추출하고, Marker Layout을 별도로 작성하여 사용했습니다.

