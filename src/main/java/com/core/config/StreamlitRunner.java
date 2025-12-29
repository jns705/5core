package com.core.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StreamlitRunner implements ApplicationRunner {

    @Value("${streamlit.mode:local}")
    private String mode;   // local 또는 docker

    @Value("${streamlit.ai-server-dir:..}")
    private String aiServerDirProp;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Path aiServerDir;
        Path pythonPath;

        if ("docker".equalsIgnoreCase(mode)) {
            // 컨테이너 안 ai-server 위치
            aiServerDir = Paths.get("/ai-server").toAbsolutePath().normalize();

            // 시스템 파이썬
            pythonPath = Paths.get("/usr/local/bin/python");

        } else {
            Path projectDir = Paths.get("").toAbsolutePath();
            Path parent = projectDir.getParent();
            if (parent == null) {
                parent = projectDir;
            }
            aiServerDir = parent.resolve("ai-server").toAbsolutePath().normalize();

            pythonPath = aiServerDir
                    .resolve(".venv")
                    .resolve("Scripts")
                    .resolve("python.exe");
        }

        if (!Files.exists(pythonPath)) {
            System.err.println("python 실행 파일이 존재하지 않음: " + pythonPath);
            return;
        }

        if (!Files.exists(aiServerDir.resolve("ai_for_dealer.py"))) {
            System.err.println("ai_for_dealer.py가 존재하지 않음: " + aiServerDir.resolve("ai_for_dealer.py"));
            return;
        }

        if (!Files.exists(aiServerDir.resolve("applyDetail.py"))) {
            System.err.println("applyDetail.py가 존재하지 않음: " + aiServerDir.resolve("applyDetail.py"));
            return;
        }

        // server.address. 추가
        ProcessBuilder builder = new ProcessBuilder(
                pythonPath.toString(),
                "-m", "streamlit", "run",
                "ai_for_dealer.py",
                "--server.baseUrlPath=5core",
                "--server.port=8509",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder applyDetailRecommend = new ProcessBuilder(
                pythonPath.toString(),
                "-m", "streamlit", "run",
                "applyDetail.py",
                "--server.baseUrlPath=5core",
                "--server.port=8502",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder dealerSaleGraph = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "sales_graph.py",
                "--server.baseUrlPath=5core",
                "--server.port=8503",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder chatbot = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "chatbot_streamlit.py",
                "--server.baseUrlPath=5core",
                "--server.port=8504",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder monthlySalesGraph = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "monthly_sales_graph.py",
                "--server.baseUrlPath=5core",
                "--server.port=8506",
                "--server.headless=true",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder globalSales = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "global_sales_dashboard.py",
                "--server.baseUrlPath=5core",
                "--server.port=8507",
                "--server.headless=true",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder modelSales = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "model_sales_dashboard.py",
                "--server.baseUrlPath=5core",
                "--server.port=8508",
                "--server.headless=true",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder chatbotAdmin = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "chatbot_admin_streamlit.py",
                "--server.baseUrlPath=5core",
                "--server.port=8510",
                "--server.headless=true",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder aiDomForecast = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "aiDomForecast.py",
                "--server.baseUrlPath=5core",
                "--server.port=8512",
                "--server.headless=true",
                "--server.address=0.0.0.0"
        );

        ProcessBuilder aiNaForecast = new ProcessBuilder(
                pythonPath.toString(), "-m", "streamlit", "run",
                "aiNaForecast.py",
                "--server.baseUrlPath=5core",
                "--server.port=8513",
                "--server.headless=true",
                "--server.address=0.0.0.0"
        );

        builder.directory(aiServerDir.toFile());
        applyDetailRecommend.directory(aiServerDir.toFile());
        dealerSaleGraph.directory(aiServerDir.toFile());
        chatbot.directory(aiServerDir.toFile());
        monthlySalesGraph.directory(aiServerDir.toFile());
        globalSales.directory(aiServerDir.toFile());
        modelSales.directory(aiServerDir.toFile());
        chatbotAdmin.directory(aiServerDir.toFile());
        aiDomForecast.directory(aiServerDir.toFile());
        aiNaForecast.directory(aiServerDir.toFile());

        builder.start();
        applyDetailRecommend.start();
        dealerSaleGraph.start();
        chatbot.start();
        monthlySalesGraph.start();
        globalSales.start();
        modelSales.start();
        chatbotAdmin.start();
        aiDomForecast.start();
        aiNaForecast.start();

        System.out.println("Streamlit이 실행됨 (" + mode + " 모드)");
    }
}
