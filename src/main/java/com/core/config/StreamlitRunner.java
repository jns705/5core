package com.core.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StreamlitRunner implements ApplicationRunner {

	// 5core를 구동하면서 streamlit을 함께 구동하도록 설정
	// - 5core와 ai-server 두 폴더가 같은 상위 폴더에 위치해야 함
	// - 예를 들어 c드라이브의 git폴더 안에 5core폴더, ai-server폴더가 함께 있어야 함
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 현재 5core 프로젝트 위치
		Path projectDir = Paths.get("").toAbsolutePath();
		
		// ai-server 폴더 위치
		// - 상위 폴더가 같으므로 getParent()를 통해서 찾을 수 있음
		Path aiServerDir = projectDir.getParent().resolve("ai-server");
		
		// venv 폴더의 python.exe 경로 (윈도우 기준)
		Path pythonPath = aiServerDir
				.resolve(".venv")
				.resolve("Scripts")
				.resolve("python.exe");
		
		if(!Files.exists(pythonPath)) {
			System.err.println("python.exe가 존재하지 않음");
			return;
		}
		
		if(!Files.exists(aiServerDir.resolve("app.py"))) {
			System.err.println("app.py가 존재하지 않음");
			return;
		}
		
		if(!Files.exists(aiServerDir.resolve("applyDetail.py"))) {
			System.err.println("applyDetail.py가 존재하지 않음");
			return;
		}
		
		// Streamlit run app.py --server.baseUrlPath=5core --server.port=8501 커맨드 실행
		ProcessBuilder builder = new ProcessBuilder(
                pythonPath.toString(),
                "-m", "streamlit", "run",
                "app.py",
                "--server.baseUrlPath=5core",
                "--server.port=8501"
        );
		
		ProcessBuilder builder2 = new ProcessBuilder(
                pythonPath.toString(),
                "-m", "streamlit", "run",
                "applyDetail.py",
                "--server.baseUrlPath=5core",
                "--server.port=8502"
        );
		// 실행 디렉토리
		builder.directory(aiServerDir.toFile());
		builder2.directory(aiServerDir.toFile());
		// 프로세스 실행
		// - 실행되었다면 console에 streamlit이 실행되었다고 뜸
		builder.start();
		builder2.start();
		System.out.println("Streamlit이 실행됨");
	}
	

}
