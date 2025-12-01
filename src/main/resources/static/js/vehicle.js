document.addEventListener("DOMContentLoaded", () => {
	
	// 색상 원 클릭 시 해당하는 색상 이미지로 변경
	const mainImage = document.getElementById("mainImage");
	const colorBtns = document.querySelectorAll(".colorCircle");
	
	colorBtns.forEach(btn => {
		btn.addEventListener("click", () => {
			
			// data-img에 저장된 파일명
			const imageName = btn.dataset.img;
			
			if(imageName) {
				mainImage.src = "/5core/images/" + imageName;
			}
		});
	});
	
	// trim을 선택하면 웹에서 보여지는 가격이 바뀜
	const btns = document.querySelectorAll("input[name='trim']");
	const priceText = document.getElementById("priceText");
	const basePrice = parseInt(priceText.dataset.base);

	btns.forEach(btn => {
	    btn.addEventListener("change", function() {

	        let addPrice = 0;

	        switch(this.value) {
	            case "Exclusive":
	                addPrice = 2000000;
	                break;
	            case "Prestige":
	                addPrice = 4000000;
	                break;
	            default:
	                addPrice = 0;
	        }

	        const result = basePrice + addPrice;
	        priceText.innerText = result.toLocaleString();
	    });
	});
});
