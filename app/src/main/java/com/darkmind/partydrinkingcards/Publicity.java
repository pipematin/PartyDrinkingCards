package com.darkmind.partydrinkingcards;

public class Publicity {
    private static boolean launch = false;

    private String test_appid ;
    private String test_interstitial;
    private String test_video;

    private String launch_appid;
    private String launch_interstitial;
    private String launch_video;

    public Publicity(){
        test_appid = "ca-app-pub-3940256099942544~3347511713";
        test_interstitial = "ca-app-pub-3940256099942544/1033173712";
        test_video = "ca-app-pub-3940256099942544/5224354917";

        launch_appid = "ca-app-pub-5144756144150507~1113125525";
        launch_interstitial = "ca-app-pub-5144756144150507/5643822509";
        launch_video = "ca-app-pub-5144756144150507/7998523103";
    }

    public String getAppid(){
        if(launch){
            return launch_appid;
        }else{
            return test_appid;
        }
    }

    public String getInterstitial(){
        if(launch){
            return launch_interstitial;
        }else{
            return test_interstitial;
        }
    }

    public String getVideo(){
        if(launch){
            return launch_video;
        }else{
            return test_video;
        }
    }

}
