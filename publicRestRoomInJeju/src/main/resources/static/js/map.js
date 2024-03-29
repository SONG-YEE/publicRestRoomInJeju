

// fetch api로 통신
const apiUrl = 'http://localhost:8080/toilet';
fetch(apiUrl)
    .then(response => response.json())
    .then(resultList => {
        console.log(':::resultList::: ', resultList);

        let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
            mapOption = { 
                center: new kakao.maps.LatLng(33.507072, 126.492770), // 지도의 중심좌표
                level: 7 // 지도의 확대 레벨
            };

        let map = new kakao.maps.Map(mapContainer, mapOption);

        // 마커 이미지 변경 (오리지널)
        let imageSrc = '/images/toilet.png', // 마커이미지 주소    
        	imageSize = new kakao.maps.Size(29, 40), // 마커이미지 크기입
        	imageOption = {offset: new kakao.maps.Point(27, 40)}; // 마커이미지 옵션. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정
        

        // 클릭된 마커 이미지
        let clickedImgSrc = '/images/clickedToilet.png',
            clickedImgSize = new kakao.maps.Size(36, 50),
            clickedImageOption = {offset: new kakao.maps.Point(27, 50)};
        
        // 원본 마커 이미지 생성
        let originalMarkerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

        // 클릭된 마커 이미지 생성
        let clickedMarkerImage = new kakao.maps.MarkerImage(clickedImgSrc, clickedImgSize, clickedImageOption);

        // 지도 줌 컨트롤 생성 (지도 div 바로 옆으로 옮기는 거 생각 중)
        let zoomControl = new kakao.maps.ZoomControl();
        map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);


        // 현위치
		// HTML5의 geolocation으로 사용할 수 있는지 확인 
		function moveToCurrentLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    let lat = position.coords.latitude,
                        lon = position.coords.longitude;
                    let locPosition = new kakao.maps.LatLng(lat, lon);
                 
                    map.setCenter(locPosition);
                });
            }
        }

        // HTML 요소에 이벤트 리스너 등록
        document.getElementById('moveToCurrentLocation').addEventListener('click', moveToCurrentLocation);

        // 마커 배열
        let markers = [];

        // 마커 찍기
        resultList.forEach(infoList => {
            let markerPosition = new kakao.maps.LatLng(infoList.laCrdnt, infoList.loCrdnt);
            
            let marker = new kakao.maps.Marker({
                position: markerPosition,
                image: originalMarkerImage
            });

            marker.setMap(map);

            markers.push(marker);

            // 마커 클릭 이벤트 등록
            kakao.maps.event.addListener(marker, 'click', function() {

                // 이전에 클릭한 마커를 원래대로 돌리기
                markers.forEach(m => {
                    m.setImage(originalMarkerImage);
                });

                // 현재 클릭된 마커 이미지 변경
                marker.setImage(clickedMarkerImage);

                // 클릭된 마커로 지도 이동 및 확대
                map.setCenter(markerPosition);
                map.setLevel(3);

                // 모달 내용 업데이트 (밑에 짜놓은 함수 활용)
                updateModalContent(infoList);
                
                // 마커 클릭하면 경도, 위도 콘솔에 뜸
                console.log('위도, 경도: ', infoList.laCrdnt, ', ', infoList.loCrdnt);
            });
        });
    })
    .catch(error => console.error('Error : ', error));

// 모달 내용 업데이트 함수
function updateModalContent(infoList) {
    let modal = document.getElementById('myModal');
    let modalImage = document.getElementById('modalImage');
    let modalInfo = document.getElementById('modalInfo');
    let infoContent = document.getElementById('infoContent');

    // 사진이 존재할 경우
    if(infoList.photoYn === 'Y' && infoList.photo.length > 0) {
        
        // 배열에서 사진 url 가져오기
        let photoUrls = infoList.photo;

        // 배열 확인
        console.log('::::::::::::::::사진배열::::::::::::::::', photoUrls);

        // 현재 보여지는 사진의 인덱스
        let currentIndex = 0;

        // 사진 이미지 업데이트
        modalImage.src = photoUrls[currentIndex];

        // 화살표 클릭 이벤트
        let prevButton = document.getElementById('prevButton');
        let nextButton = document.getElementById('nextButton');

        prevButton.addEventListener('click', function() {
            currentIndex = (currentIndex -1 + photoUrls.length) % photoUrls.length;
            modalImage.src = photoUrls[currentIndex];

        });

        nextButton.addEventListener('click', function() {
            currentIndex = (currentIndex + 1) % photoUrls.length;
            modalImage.src = photoUrls[currentIndex];

        });

    } else {
        // 사진이 없으면 이미지 영역 초기화
        modalImage.src = '';
    }


    // etcCn(기타사항) 값이 있으면 업데이트, 없으면 없음 표시
    let etcCnHTML = '';
    if (infoList.etcCn) {
        etcCnHTML = `<span>특이사항 : ${infoList.etcCn}</span>`;
    } else {
        etcCnHTML = `<span>특이사항 : 없음</span>`;
    }

    // 정보 업데이트
    infoContent.innerHTML = `
        <div>
            <h1 style="font-weight:bold">
                ${infoList.toiletNm} 화장실 <br>
            </h1>
            <h2>
                (${infoList.emdNm}) <br><br>
            	${infoList.rnAdres} <br>
            </h2>
            <span>(${infoList.lnmAdres}) <br><br><br></span>
            <span>
                개방 시간 : ${infoList.opnTimeInfo} <br>
                남녀 공용 : ${infoList.mwmnCmnuseToiletYn}<br>
                ${etcCnHTML}
            </span>
        </div>`;

    // 모달 표시
    modal.style.display = 'block';
}


// 모달 닫기 함수
function closeModal() {
    document.getElementById('myModal').style.display = "none";
}