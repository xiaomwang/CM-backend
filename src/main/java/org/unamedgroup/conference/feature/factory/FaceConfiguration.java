package org.unamedgroup.conference.feature.factory;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 创建FaceEngine的工厂
 * FaceEngineFactory
 * @author zhoutao
 * @date 2019-03-12
 */
@Configuration
public class FaceConfiguration {

    @Bean
    public  FaceEngine faceEngine() {

        //windows
//        String appId = "Bbvyu5GeUE8eaBhyLsNcp48RNJ4mBnFHHKAuAgRpJvDT";
//        String sdkKey = "6qjPs9Ey8zBbshQnkc3jpXLMXf8jMtNbXQpnCYSAQuqJ";
        //linux
        String appId = "3NapBq4mgBbQAwwiSPyLDsB4mevNYtUxR8m2YXx4PYur";
        String sdkKey = "j4UamkGDadxoeSnkkMWMSKfbrR7pCajXmLhhg4rLGjV";

        FaceEngine faceEngine = new FaceEngine();
        //激活引擎
        faceEngine.active(appId, sdkKey);

        EngineConfiguration engineConfiguration = EngineConfiguration.builder().functionConfiguration(
                FunctionConfiguration.builder()
                        .supportAge(true)
                        .supportFace3dAngle(true)
                        .supportFaceDetect(true)
                        .supportFaceRecognition(true)
                        .supportGender(true)
                        .build()).build();
        //初始化引擎
        faceEngine.init(engineConfiguration);
        return faceEngine;
    }



}
