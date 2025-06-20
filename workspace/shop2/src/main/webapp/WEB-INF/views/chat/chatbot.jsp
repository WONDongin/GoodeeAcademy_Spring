<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AI 챗봇</title>
    <link href="/css/chatbot.css" rel="stylesheet">
</head>
<body>
    <div id="chatbotarea" class="text-center rounded" style="background-color:#1A6511">
        <label for="gptarea" class="text-light fw-bold">AI 챗봇</label>
        <div id="gptanswerarea" class="chatbot" style="background-color: #A8C0D6;">
            <div class="chat bot">
                <div class="icon"><i></i></div>
                <div class="textbox text-start">
                    AI 챗봇입니다. 궁금하신 점을 문의해 주세요.
                </div>
            </div>
        </div>
    </div>

    <hr>

    <div id="gptarea">
        <textarea id="gpt_question" rows="2" class="form-control"></textarea>
    </div>
    <div class="text-end d-flex justify-content-between">
        <button class="btn btn-primary text-light" onclick="gptquestion()">AI 문의</button>
    </div>

    <br>

    <script type="text/javascript">
        let gptInput = document.querySelector("#gpt_question");

        gptInput.addEventListener("keydown", (e) => {
            if (e.keyCode === 13) {
                gptquestion();
            }
        });

        function gptquestion() {
            let question = document.querySelector("#gpt_question").value.trim();

            if (question === '') {
                alert("AI 챗봇에게 질문할 내용을 입력해 주세요.");
                document.querySelector("#gpt_question").focus();
                return;
            }

            if (question.length < 10) {
                alert("AI 챗봇에게 질문할 내용을 좀 더 자세히 입력해 주세요.");
                document.querySelector("#gpt_question").focus();
                return;
            }

            let userHtml = "<div class='chat me'><div class='icon'><i></i></div>" +
                           "<div class='textbox'>" + question + "</div></div>";

            document.querySelector("#gptanswerarea").innerHTML += userHtml;
            document.querySelector("#gpt_question").value = "";

            let gptanswerarea = document.querySelector("#gptanswerarea");
            gptanswerarea.scrollTop = gptanswerarea.scrollHeight;

            let paramdata = {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" },
                cache: "no-cache",
                referrerPolicy: "no-referrer",
                body: "question=" + encodeURIComponent(question)
            };

            fetch('/ajax/gptquestion', paramdata)
                .then(response => response.text())
                .then(gptdata => {
                    let botHtml = "<div class='chat bot'><div class='icon'><i></i></div>" +
                                  "<div class='textbox'>" +
                                  gptdata.replaceAll("\n", "<br>").replaceAll(" ", "&nbsp;") +
                                  "</div></div></div>";

                    document.querySelector("#gptanswerarea").innerHTML += botHtml;
                    gptanswerarea.scrollTop = gptanswerarea.scrollHeight;
                });
        }
    </script>
</body>
</html>
