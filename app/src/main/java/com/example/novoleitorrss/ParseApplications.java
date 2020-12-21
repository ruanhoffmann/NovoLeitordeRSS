package com.example.novoleitorrss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;
    public ParseApplications(){
        applications = new ArrayList<>();

    }

    //criando o getter do array, pq queremos acessar do lado de fora tbm;
    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlText){
        boolean status = true;//parse com sucesso?
        FeedEntry entry = null;//entrada do RSS a ser lida
        boolean inEntry = false;//estamos em um <entry>?
        String textValue = "";//valor do texto de cada atributo;

        XmlPullParserFactory factory = null;

        try {
            factory = XmlPullParserFactory.newInstance();//trás a instãncia
            factory.setNamespaceAware(true);
            XmlPullParser pullParser = factory.newPullParser();//o cara que faz o parser pra gente.
            pullParser.setInput(new StringReader(xmlText));//pega aquele texto e usa ele como entrada para fazer o parse;

            //começa a fazer o parse;

            int eventType = pullParser.getEventType();
            // enquanto não for o fim do documento, tratar tag por tag
            while (eventType != XmlPullParser.END_DOCUMENT){
            String tag = pullParser.getName();
            switch (eventType){
                case XmlPullParser.START_TAG:
                    Log.d(TAG, "parse: Começando a tag: " +tag);
                    if ("entry".equalsIgnoreCase(tag)){
                        inEntry = true;
                        entry = new FeedEntry();

                    }
                    break;
                case XmlPullParser.TEXT://se for um tipo TEXT, vou pegar o texto dessa TAG;
                    textValue = pullParser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    Log.d(TAG, "parse: Terminando a TAG: " + tag);
                    if (inEntry){
                        if ("entry" .equalsIgnoreCase(tag)){//se for um </entry>, eu termino minha aplicação;
                            //TERMINOU A 'entry', ENTÃO ARMAZENAR O FEEDENTRY NA ARRAYLIST
                            applications.add(entry);
                            inEntry = false;
                        }else if ("name".equalsIgnoreCase(tag)){// se a tag for uma </name>
                                entry.setName(textValue);
                        }else if ("artist".equalsIgnoreCase(tag)){// se a tag for uma </artist>
                                entry.setArtist(textValue);
                        }else if ("summary".equalsIgnoreCase(tag)){// se a tag for uma </summary>
                                entry.setSummary(textValue);
                        }else if ("image".equalsIgnoreCase(tag)){// se a tag for uma </image>
                                entry.setImgURL(textValue);
                        }else if ("releaseDate".equalsIgnoreCase(tag)){// se a tag for uma </releaseDate>
                                entry.setReleaseDate(textValue);
                        }
                        break;
                    }

            }

                eventType = pullParser.next();
            }

        }catch (Exception ex){
            Log.e(TAG, "parse: Erro ao fazer parse:"+ ex.getMessage());
            status = false;
        }

        return status;
    }
}
