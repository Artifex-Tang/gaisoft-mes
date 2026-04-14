package com.gaisoft.ai.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(value={"/deepseek/ai"})
public class AiDeepseekController {
    private static final String API_KEY = "sk-d7aa1f72e85e4753b77e68019e91e734";
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    @PostMapping(value={"/streamChat"})
    public SseEmitter streamChat(@org.springframework.web.bind.annotation.RequestBody Map<String, String> body) {
        String question = body.get("question");
        System.out.println("============" + question);
        final SseEmitter emitter = new SseEmitter(Long.valueOf(0L));
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0L, TimeUnit.SECONDS).build();
        MediaType mediaType = MediaType.get((String)"application/json; charset=utf-8");
        String requestBodyString = "{\"model\":\"deepseek-chat\",\"stream\":true,\"messages\":[{\"role\":\"user\",\"content\":\"" + question + "\"}]}";
        RequestBody requestBody = RequestBody.create((MediaType)mediaType, (String)requestBodyString);
        Request request = new Request.Builder().url(API_URL).addHeader("Authorization", "Bearer sk-d7aa1f72e85e4753b77e68019e91e734").post(requestBody).build();
        client.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                try {
                    emitter.send(SseEmitter.event().data((Object)("Error: " + e.getMessage())));
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                emitter.completeWithError((Throwable)e);
            }

            public void onResponse(Call call, Response response) {
                try (ResponseBody responseBody = response.body();){
                    if (responseBody != null && response.isSuccessful()) {
                        BufferedSource source = responseBody.source();
                        while (!source.exhausted()) {
                            String line = source.readUtf8LineStrict();
                            if (!line.startsWith("data: ")) continue;
                            String json = line.substring(6);
                            System.out.println("[SSE json] " + json);
                            if (!"[DONE]".equals(json)) {
                                emitter.send(SseEmitter.event().data((Object)json));
                                continue;
                            }
                            break;
                        }
                    } else {
                        emitter.send(SseEmitter.event().data((Object)("Error: " + response.code())));
                    }
                    emitter.complete();
                }
                catch (IOException e) {
                    emitter.completeWithError((Throwable)e);
                }
            }
        });
        return emitter;
    }
}
