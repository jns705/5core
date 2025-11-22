$(document).ready(() => {	
	let fileNameList = [
			"main_kv_ioniq6_n_pc.png","main_sonata_25my_w.png",
			"Main-KV_Car_IONIQ-5-N.png", "main-santafe-25my-kv-w.png", 
			"Main-KV_Car_TUCSON.png","main_kv_ioniq6_pc.png",
			"Main-KV_Car_CASPER-Electric.png"
		];
		  
	let imgList = document.querySelectorAll(".top-img"); // 배열
	for (let i = 0; i < imgList.length; i++) {
		console.log(i);
		imgList[i].style.backgroundImage = "url(/5core/images/" + fileNameList[i] + ")";
	}
	  
  // bxSlider에 들어가 이미지 생성
  $(".slider1").bxSlider({
      slideWidth: 5000,           // 슬라이드의 너비
      slideHeder: 500,           // 슬라이드의 높이
      maxSlides: 1,              // 최대 노출 개수
      minSlides: 1,              // 최소 노출 개수
      slideMargin: 20,           // 슬라이드의 좌우 마진
      moveSlides: 1,             // 슬라이드 이동개수
      auto: true,                // 자동 화면전환 여부: 기본값(flase)
      speed: 1000,               // 화면이동 시간
      pause: 4000,               // 화면전환 시간: 이동(1초) + 지연(3초) = 4초
      controls: true,            // 이전, 다음 버튼 유무: 기본값(true)
      autoControls: false,       // 재생, 정지 버튼 유무: 기본값(false)
      pager: true,               // 블릿의 유무: 기본값(true)
      autoHover: true,           // 마우스를 올렸을 때 화면전환 여부, 기본값(false)
      stopAutoOnClick: false,     //  블릿을 클릭했을 때 화면 전환 여부,기본값(false)
      infiniteLoop: true,        // 화면전환 무한루프 여부: 기본값(true)
      // 추가 옵션
      mode: "horizontal",        // 화면전환 방향: horizontal(가로, 기본값), vertical(세로)
      autoDirection: "next",     // 화면전환 진행 방향: next(기본값, 오른쪽에서 왼쪽으로 이동), previous(왼쪽에서 오른쪽으로 이동)
      startSlider: 0,            // 슬라이드 시작 인덱스: 기본값(0), 인덱스는 0번부터 시작
      randomStart: false,         // 슬라이드 시작 번호 랜덤 여부: 기본값(flase)
  }) 
})