package com.cpalead.app.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class DeviceInfo {

    final Context context;
    final HashMap<String, String> model_list;
    final ArrayList<String> version_list = new ArrayList<String>() {
        {
            add("6.0.1");
            add("7.0.1");
            add("8");
            add("9");
            add("10");
        }
    };
    final List<String> useragent_list = new ArrayList<>();
    final Set<String> keySet;
    final List<String> keyList;
    final int keySize;
    final Random random = new Random();
    String imei, androidId, advertisingId, macAddress, gcmId, uuId, simSerial, mobile, model, build, fingerPrint;

    public DeviceInfo(Context context) {
        this.context = context;

        // Set Model
        model_list = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("devices.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] deviceArray = line.split(":");
                model_list.put(deviceArray[0], deviceArray[1]);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        keySet = model_list.keySet();
        keyList = new ArrayList<>(keySet);
        keySize = keyList.size();

        // Set Useragent
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("useragents.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String useragent = line.trim();
                useragent_list.add(useragent);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {

        if (imei.equals("false")) {

            this.imei = (random.nextInt(99999 - 11111) + 11111) + "" + (random.nextInt(99999 - 11111) + 11111) + "" + (random.nextInt(99999 - 11111) + 11111);

        } else {
            this.imei = imei;
        }
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {

        if (androidId.equals("false")) {

            ArrayList<String> char_list = new ArrayList<>();
            char_list.add("a");
            char_list.add("b");
            char_list.add("c");
            char_list.add("d");
            char_list.add("e");
            char_list.add("f");
            int list_size = char_list.size();

            this.androidId = char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size));

        } else {

            this.androidId = androidId;
        }
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {

        if (advertisingId.equals("false")) {
            ArrayList<String> char_list = new ArrayList<>();
            char_list.add("a");
            char_list.add("b");
            char_list.add("c");
            char_list.add("d");
            char_list.add("e");
            char_list.add("f");
            int list_size = char_list.size();

            this.advertisingId = char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + '-' + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + '-' + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + '-' + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + '-' + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size)) + "" + char_list.get(random.nextInt(list_size)) + "" + (random.nextInt(9)) + "" + char_list.get(random.nextInt(list_size));
        } else {

            this.advertisingId = advertisingId;
        }
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {

        if (macAddress.equals("false")) {

            ArrayList<String> char_list = new ArrayList<>();
            char_list.add("A");
            char_list.add("B");
            char_list.add("C");
            char_list.add("D");
            char_list.add("E");
            char_list.add("F");
            int list_size = char_list.size();

            this.macAddress = char_list.get(random.nextInt(list_size)) + (random.nextInt(9)) + ':' + char_list.get(random.nextInt(list_size)) + (random.nextInt(9)) + ':' + char_list.get(random.nextInt(list_size)) + (random.nextInt(9)) + ':' + (random.nextInt(9)) + char_list.get(random.nextInt(list_size)) + ':' + char_list.get(random.nextInt(list_size)) + (random.nextInt(9)) + ':' + char_list.get(random.nextInt(list_size)) + char_list.get(random.nextInt(list_size));

        } else {
            this.macAddress = macAddress;
        }
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {

        if (gcmId.equals("false")) {

            String only_characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            char[] only_charactersArray = only_characters.toCharArray();
            int only_charactersLength = only_characters.length();

            String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";
            char[] charactersArray = characters.toCharArray();
            int charactersLength = characters.length();

            StringBuilder gcmIdStr = new StringBuilder();

            for (int i = 0; i <= 11; i++) {
                gcmIdStr.append(only_charactersArray[random.nextInt(only_charactersLength)]);
            }

            gcmIdStr.append(":APA");

            for (int i = 0; i <= 137; i++) {
                gcmIdStr.append(charactersArray[random.nextInt(charactersLength)]);
            }

            this.gcmId = gcmIdStr.toString();
        } else {

            this.gcmId = gcmId;
        }
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {

        if (uuId.equals("false")) {
            this.uuId = UUID.randomUUID().toString();
        } else {
            this.uuId = uuId;
        }
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(String simSerial) {

        if (simSerial.equals("false")) {
            this.simSerial = (random.nextInt(99999 - 11111) + 11111) + "" + (random.nextInt(99999 - 11111) + 11111) + "" + (random.nextInt(99999 - 11111) + 11111) + "" + (random.nextInt(99999 - 11111) + 11111);
        } else {
            this.simSerial = simSerial;
        }
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {

        if (mobile.equals("false")) {
            this.mobile = (random.nextInt(9 - 6) + 6) + "" + (random.nextInt(9 - 7) + 7) + "" + (random.nextInt(99 - 55) + 55) + "" + (random.nextInt(999999 - 111111) + 111111);
        } else {
            this.mobile = mobile;
        }
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        if (model.equals("false")) {
            this.model = keyList.get(random.nextInt(keySize));
        } else {
            this.model = model;
        }
    }

    public String getVersion() {
        return version_list.get(random.nextInt(version_list.size()));
    }

    public String getBrand(String brand) {
        return model_list.get(brand);
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {

        if (build.equals("false")) {
            String only_characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            char[] only_charactersArray = only_characters.toCharArray();
            int only_charactersLength = only_characters.length();

            StringBuilder buildStr = new StringBuilder();

            for (int i = 0; i <= 5; i++) {
                buildStr.append(only_charactersArray[random.nextInt(only_charactersLength)]);
            }

            this.build = buildStr.toString();
        } else {
            this.build = build;
        }
    }

    public String getUserAgent() {
        return useragent_list.get(random.nextInt(useragent_list.size()));
    }

    public String getFingerPrint(String model, String brand, String version, String build) {
        return brand.toLowerCase().replaceAll("\\s+", "") + "/" + model.toLowerCase().replaceAll("\\s+", "") + "/" + model.toLowerCase().replaceAll("\\s+", "") + ":" + version + "/" + build + "/" + random.nextInt((99999999 - 11111111) + 11111111) + ":user/release-keys";
    }
}
