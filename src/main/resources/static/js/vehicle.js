// 색상 예시 클릭 시 해당하는 색상 이미지로 변경
document.addEventListener("DOMContentLoaded", () => {
	const mainImage = document.getElementById("mainImage");
	const colorBtns = document.querySelectorAll(".color-circle");
	
	colorBtns.forEach(btn => {
		btn.addEventListener("click", () => {
			
			// data-img에 저장된 파일명
			const imageName = btn.dataset.img;
			
			if(imageName) {
				mainImage.src = "/images/" + imageName;
			}
			console.log(document.getElementById("mainImage").src);
			console.log("이미지 경로:", "/images/" + imageName);
		});
	});
});