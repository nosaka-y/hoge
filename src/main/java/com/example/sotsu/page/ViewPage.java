package com.example.sotsu.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.springframework.ui.Model;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MountPath("View")
public class ViewPage extends WebPage {

    File file;
    int count = 0;
    ArrayList<List<String>> text;

    public ViewPage() {
        add(new form("f"));
    }
    public ViewPage(File f, int c) throws IOException {
        this.file = f;
        this.count = c;
        add(new form("f"));
        text = getfile(file);
        text.remove(0);
        add(new Label("id", text.get(count).get(0)));
    }

    private class form extends Form<Model> {
        public form(String id) {
            super(id);
            add(new Button("CountDown"){
                @Override
                public void onSubmit(){
                    count = (count - 1 + text.size() - 1) % (text.size() - 1);
                    System.out.println(count);
                    try {
                        setResponsePage(new ViewPage(file, count));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            add(new Button("CountUp"){
                @Override
                public void onSubmit(){
                    count = (count + 1) % (text.size() - 1);
                    System.out.println(count);
                    try {
                        setResponsePage(new ViewPage(file, count));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public ArrayList<List<String>> getfile(File file) throws IOException {
        var k = fileToStrings(file);
        //System.out.println(k);
        var r = stringsToList(k);
        //System.out.println(r);
        return r;
    };

    public static ArrayList<String> fileToStrings(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data;
        ArrayList<String> sb = new ArrayList<>();
        while ((data = bufferedReader.readLine()) != null) {
            sb.add(data);
        }

        // 最後にファイルを閉じてリソースを開放する
        bufferedReader.close();
        return sb;
    }

    public static ArrayList<List<String>> stringsToList(ArrayList<String> strings) {
        ArrayList<List<String>> result = new ArrayList<>();;
        for(int i = 0; i < strings.size(); i++) {
            result.add(Arrays.asList(strings.get(i).split("\\s*,\\s*")));
        }
        return result;
    }
}
