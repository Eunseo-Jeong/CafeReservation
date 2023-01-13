# CafeReservation (customer ver.)

<div>
        <b>Project name: CafeReservation (2020.03 ~ 2020.06)</b><br>
        <b>Explanation: 카페 좌석을 예약하는 시스템 (안드로이드 기반 어플)</b><br><br>
        <b> * 해당 Repo는 좌석을 예약하는 고객용 입니다.</b><br><br>
        <b> 데이터베이스를 위한 서버는 Firebase를 사용했습니다.</b><br>
</div><br>


## Screenshot

<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212267752-7ff242c3-7699-4681-baa8-1e389dddb0ef.png">

<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212269729-097cb362-914c-487e-ac68-d9deaab810ad.png">

<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212270716-3ba0cbf0-773b-4781-a4a2-ca645767a10d.png">


<br>

## Function
#### 1. Login & Logout
* Firebase 서버 연동을 통해, 로그인과 로그아웃이 가능하도록 하였습니다.

#### 2. Table Reservation & Check
* Customer는 좌석을 예약할 수 있고, Manager는 좌석을 관리할 수 있습니다.
* 좌석을 이용하는 사용자에 대한 정보는 Firebase에 저장됩니다.
* 이를 통해 예약을 할 수 있는 좌석(빈 좌석)과, 예약을 할 수 없는 좌석(사용중인 좌석)을 구분할 수 있습니다. 

#### 3. Reservation Check & Cancel
* Manager를 위한 기능으로, 예약을 했지만 오지 않은 사용자로 인해 빈 좌석은 취소됩니다.
* Manager는 모든 좌석에 접근할 수 있고 예약 현황을 파악할 수 있습니다.
* 이를 통해 전 좌석을 관리합니다.


<br>

## Non-Function
#### 1. Usability
<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212273761-a02f4e97-f6c7-4f1c-9d9b-02336e216b68.png">

#### 2. Reliability & Performance
<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212274022-a545d903-5dd5-4283-b930-c484276d5681.png">

#### 3. System Modeling
* Class
<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212274144-db8030df-2f0d-48b8-ac57-ebbec2b79ba7.png">

* Process
<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212274194-12b7daa5-a14a-4388-9a6e-b5a27f1518b8.png">
<img width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212274329-165fcec2-2cfe-4366-be4d-c7913e3c74ba.png">

* Use case
<img  width="500" hight="400" alt="image" src="https://user-images.githubusercontent.com/64178197/212274509-a4cfa365-3639-4d0d-8133-3283f4b8fad3.png">




