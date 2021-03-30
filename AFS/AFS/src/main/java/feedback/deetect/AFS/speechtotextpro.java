package feedback.deetect.AFS;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.BaseService;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.RecognizeWithWebsocketsOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.speech_to_text.v1.websocket.SpeechToTextWebSocketListener;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import org.json.JSONObject;
//import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class speechtotextpro  {

    void xyz() {

        IamAuthenticator authenticator = new IamAuthenticator("eQP6pIm37_wtN-mpRcfKNCXmblO6VD8tw2d_NwuEaHt5");
        SpeechToText speechToText = new SpeechToText(authenticator);
        speechToText.setServiceUrl("https://api.eu-gb.speech-to-text.watson.cloud.ibm.com/instances/f45a944c-c1c4-4dd4-a90b-bd00f59003c5");

        try {
            RecognizeWithWebsocketsOptions recognizeOptions = new RecognizeWithWebsocketsOptions.Builder()
                    .audio(new FileInputStream("C:\\Users\\aman\\Downloads\\AFS\\AFS\\src\\main\\resources\\audio_file2.flac"))
                    .contentType("audio/flac")
                    .model("en-US_BroadbandModel")
                    .keywords(Arrays.asList("colorado", "tornado", "tornadoes"))
                    .keywordsThreshold((float) 0.5)
                    .maxAlternatives(3)
                    .build();

            BaseRecognizeCallback baseRecognizeCallback =
                    new BaseRecognizeCallback() {

                        @Override
                        public void onTranscription
                                (SpeechRecognitionResults speechRecognitionResults) {
                            String s1 = "";
                            for(int i=0;i<speechRecognitionResults.getResults().size();i++)
                                s1=s1+speechRecognitionResults.getResults().get(i).getAlternatives().get(0).getTranscript()+".";

                            IamAuthenticator authenticator = new IamAuthenticator("ZQmWK0VJUppDrIwH65kRVp0neBg5KXFyeZ3v1irZbR1u");
                            ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", authenticator);
                            toneAnalyzer.setServiceUrl("https://api.eu-gb.tone-analyzer.watson.cloud.ibm.com/instances/eb84bf86-32d1-4466-a7fe-72ce325a37ff");



                            ToneOptions toneOptions = new ToneOptions.Builder()
                                    .text(s1)
                                    .build();

                            ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute().getResult();
                            System.out.println(toneAnalysis);
                            String a[]= new String[7];
                            double doctone[]= new double[7];

                            a[0]="Anger"; a[1]="Fear"; a[2]="Joy"; a[3]="Sadness"; a[4]="Analytical"; a[5]="Confident"; a[6]="Tentative";
                            Map<String, Integer> map=new HashMap<String,Integer>();
                            map.put("Anger",0);
                            map.put("Fear",1);
                            map.put("Joy",2);
                            map.put("Sadness",3);
                            map.put("Analytical",4);
                            map.put("Confident",5);
                            map.put("Tentative",6);

                            for(int i=0;i<toneAnalysis.getDocumentTone().getTones().size();i++){
                                int i1=map.get(toneAnalysis.getDocumentTone().getTones().get(i).getToneName());
                                doctone[i1]=toneAnalysis.getDocumentTone().getTones().get(i).getScore();
                                //System.out.println(doctone[i1]);
                            }

                            int n=toneAnalysis.getSentencesTone().size();
                            double Anger[]= new double[n];
                            double Fear[]= new double[n];
                            double Joy[]= new double[n];
                            double Sadness[]= new double[n];
                            double Analytical[]= new double[n];
                            double Confident[]= new double[n];
                            double Tentative[]= new double[n];

                            for(int i=0;i<n;i++){
                                for(int j=0;j<toneAnalysis.getSentencesTone().get(i).getTones().size();j++){
                                    String match=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getToneName();

                                    if(map.get(match)==0)
                                        Anger[i]=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();
                                    else if(map.get(match)==1)
                                        Fear[i]=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();
                                    else if(map.get(match)==2) {
                                        //System.out.println(match);
                                        Joy[i] = toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();
                                    }
                                    else if(map.get(match)==3)
                                        Sadness[i]=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();
                                    else if(map.get(match)==4)
                                        Analytical[i]=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();
                                    else if(map.get(match)==5)
                                        Confident[i]=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();
                                    else if(map.get(match)==6)
                                        Tentative[i]=toneAnalysis.getSentencesTone().get(i).getTones().get(j).getScore().doubleValue();

                                }
                            }
                            /*for(int i=0;i<n;i++)
                                System.out.print(Anger[i]+" ");
                            System.out.println("");
                            for(int i=0;i<n;i++)
                                System.out.print(Fear[i]+" ");
                            System.out.println("");
                            for(int i=0;i<n;i++)
                                System.out.print(Joy[i]+" ");
                            System.out.println("");
                            for(int i=0;i<n;i++)
                                System.out.print(Sadness[i]+" ");
                            System.out.println("");
                            for(int i=0;i<n;i++)
                                System.out.print(Analytical[i]+" ");
                            System.out.println("");
                            for(int i=0;i<n;i++)
                                System.out.print(Confident[i]+" ");
                            System.out.println("");
                            for(int i=0;i<n;i++)
                                System.out.print(Tentative[i]+" ");
                            System.out.println("");*/

                        }

                        @Override
                        public void onDisconnected() {
                            System.exit(0);
                        }

                    };

            speechToText.recognizeUsingWebSocket(recognizeOptions,
                    baseRecognizeCallback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
