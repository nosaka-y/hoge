package com.example.sotsu.page;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Bytes;
import org.springframework.ui.Model;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@MountPath("Upload")
public class UploadPage extends WebPage {

    private FileUploadField uploadField;
    private File uploadDir;
    private File[] file;

    private FileUploadField uploadField2;
    private File uploadDir2;
    private File[] file2;

    private File[] file3 = new File[1];

    public UploadPage(){
        // Formを拡張したUpload用フォーム
        add(new UploadForm("uploadForm"));
        add(new UploadForm2("uploadForm2"));
        add(new UploadForm3("uploadForm3"));

        // アップロードディレクトリからファイルのリスト（配列）を取得
        file = uploadDir.listFiles();
        file2 = uploadDir2.listFiles();
        file3 = new File[1];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(timestamp);
        file3[0] = new File(str + ".csv");

        // ダウンロード用リンクのリスト
        add(new DownloadListView("downloadList", Arrays.asList(file)));
        add(new DownloadListView2("downloadList2", Arrays.asList(file2)));
        add(new DownloadListView3("downloadList3", Arrays.asList(file3)));

        add(new BookmarkablePageLink<>("toViewUpload", ViewUploadPage.class));
    }

    private File getDirectory(String dirName){
        WebApplication application = (WebApplication)getApplication();
        // 絶対パスの取得　保存などの操作で相対パスは、Webアプリケーションでは危険
        String uploadPath = application.getServletContext().getRealPath(dirName);
        File dir = new File(uploadPath);

        if(!dir.exists())
            dir.mkdir();

        return dir;
    }

    private class DownloadListView extends ListView{
        public DownloadListView(String id, List list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem item) {
            final File file = (File)item.getModelObject();
            // ダウンロード用リンクの作成
            DownloadLink link = new DownloadLink("downloadLink", file){
                @Override
                public void onComponentTagBody(MarkupStream markupStream,
                                                  ComponentTag openTag) {
                    // アンカータグの中身（<a href="#">～</a>　の　～の部分）をファイルの名前で置き換える
                    replaceComponentTagBody(markupStream, openTag, file.getName());
                }
            };
            item.add(link);
        }
    }
    private class DownloadListView2 extends ListView{
        public DownloadListView2(String id, List list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem item) {
            final File file = (File)item.getModelObject();
            // ダウンロード用リンクの作成
            DownloadLink link = new DownloadLink("downloadLink", file){
                @Override
                public void onComponentTagBody(MarkupStream markupStream,
                                               ComponentTag openTag) {
                    // アンカータグの中身（<a href="#">～</a>　の　～の部分）をファイルの名前で置き換える
                    replaceComponentTagBody(markupStream, openTag, file.getName());
                }
            };
            item.add(link);
        }
    }
    private class DownloadListView3 extends ListView{
        public DownloadListView3(String id, List list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem item) {
            final File file = (File)item.getModelObject();
            // ダウンロード用リンクの作成
            DownloadLink link = new DownloadLink("downloadLink", file){
                @Override
                public void onComponentTagBody(MarkupStream markupStream,
                                               ComponentTag openTag) {
                    // アンカータグの中身（<a href="#">～</a>　の　～の部分）をファイルの名前で置き換える
                    replaceComponentTagBody(markupStream, openTag, file.getName());
                }
            };
            item.add(link);
        }
    }
    private class UploadForm extends Form<Model>{
        public UploadForm(String id) {
            super(id);
            // FileUploadFieldを使う場合にFormに必要なセッティング
            setMultiPart(true);
            // ファイルサイズ制限の設定
            setMaxSize(Bytes.megabytes(2));

            uploadField = new FileUploadField("uploadField");
            add(uploadField);

            // アップロードするディレクトリ（場所）の取得
            uploadDir = getDirectory("upload");

            add(new Button("uploadButton"){
                @Override
                public void onSubmit(){
                    FileUpload upload = uploadField.getFileUpload();
                    if(upload == null){
                        // FeedbackPanelコンポーネントを使用する場合のメッセージ
                        error("アップロードするファイルが見つかりません");
                        return;
                    }

                    String fileName = upload.getClientFileName();
                    // 空ファイルをアップロードするディレクトリへ作成
                    File uploadFile = new File(uploadDir, fileName);

                    try{
                        // 空ファイルへアップロードファイルを書き出す
                        upload.writeTo(uploadFile);
                    }catch(Exception e){
                        this.error(e.getMessage());
                    }finally{
                        upload.closeStreams();
                    }
                    setResponsePage(UploadPage.class);
                }
            });
        }
    }
    private class UploadForm2 extends Form<Model>{
        public UploadForm2(String id) {
            super(id);
            // FileUploadFieldを使う場合にFormに必要なセッティング
            setMultiPart(true);
            // ファイルサイズ制限の設定
            setMaxSize(Bytes.megabytes(2));

            uploadField2 = new FileUploadField("uploadField2");
            add(uploadField2);

            // アップロードするディレクトリ（場所）の取得
            uploadDir2 = getDirectory("upload2");

            add(new Button("uploadButton2"){
                @Override
                public void onSubmit(){
                    FileUpload upload = uploadField2.getFileUpload();
                    if(upload == null){
                        // FeedbackPanelコンポーネントを使用する場合のメッセージ
                        error("アップロードするファイルが見つかりません");
                        return;
                    }

                    String fileName = upload.getClientFileName();
                    // 空ファイルをアップロードするディレクトリへ作成
                    File uploadFile = new File(uploadDir2, fileName);

                    try{
                        // 空ファイルへアップロードファイルを書き出す
                        upload.writeTo(uploadFile);
                    }catch(Exception e){
                        this.error(e.getMessage());
                    }finally{
                        upload.closeStreams();
                    }
                    setResponsePage(UploadPage.class);
                }
            });
        }
    }

    private class UploadForm3 extends Form<Model>{
        public UploadForm3(String id) {
            super(id);

            add(new Button("MergeButton") {
                @Override
                public void onSubmit() {
                    System.out.println("click");
                    try {
                        makeCsv();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setResponsePage(UploadPage.class);
                }
            });
        }
    }

    public void makeCsv() throws IOException {
        // 結果のcsvを合わせる
        ArrayList<List<String>> result = new ArrayList<>();
        try {
            ArrayList<List<String>> tmp = new ArrayList<>();
            for(int f = 0; f < file.length; f++) {
                tmp = getfile(file[f]);
                tmp.remove(0);
                for(int i = 0; i < tmp.size(); i++) {
                    if (tmp.get(i).get(0).startsWith("(")) {
                        tmp.subList(i, tmp.size()).clear();
                        result.addAll(tmp);
                        break;
                    }
                    else {
                        while(tmp.get(i).size() < 8) {
                            var t = tmp.get(i);
                            t = new ArrayList<>(t);
                            t.add("-");
                            tmp.set(i, t);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // id重複削除
        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(i).get(0).equals(result.get(j).get(0))) {
                    result.remove(j);
                    j--;
                }
            }
        }

        // 合計値とベロシティ計算
        for (int i = 0; i < result.size(); i++) {
            int sum = 0;
            for (int j = 0; j < 7; j++) {
                if (result.get(i).get(result.get(i).size() - j - 1).equals("-")) {
                    continue;
                }
                sum += Integer.parseInt(result.get(i).get(result.get(i).size() - j - 1));
            }
            var tmp = result.get(i);
            tmp = new ArrayList<>(tmp);
            tmp.add(String.valueOf(sum));
            tmp.add(String.valueOf(sum / 7));
            result.set(i, tmp);
        }

        // 目標設定のcsv
        var result2 = getfile(file2[0]);
        result2.remove(0);
        // id重複削除
        for (int i = result2.size() - 1; i > -1; i--) {
            for (int j = i - 1; j > -1; j--) {
                if (result2.get(i).get(0).equals(result2.get(j).get(0))) {
                    result2.remove(j);
                    i--;
                }
            }
        }
        // 合計値計算
        for (int i = 0; i < result2.size(); i++) {
            int sum = 0;
            for (int j = 0; j < 7; j++) {
                sum += Integer.parseInt(result2.get(i).get(result2.get(i).size() - j - 1));
            }
            var tmp = result2.get(i);
            tmp = new ArrayList<>(tmp);
            tmp.add(String.valueOf(sum));
            result2.set(i, tmp);
        }

        // 目標設定と結果を一つにする
        ArrayList<List<String>> csv = result;
        for (int i = 0; i < csv.size(); i++) {
            boolean ok = false;
            // id基準で目標と合わせる
            for (int j = 0; j < result2.size(); j++) {
                if (csv.get(i).get(0).equals(result2.get(j).get(5))) {
                    ok = true;
                    var tmp = csv.get(i);
                    tmp = new ArrayList<>(tmp);
                    var tmp2 = result2.get(j);
                    tmp2 = new ArrayList<>(tmp2);
                    tmp2 = tmp2.subList(6, 14);

                    tmp.addAll(1, tmp2);
                    csv.set(i, tmp);
                    break;
                }
            }
            // 目標設定が無い場合
            if (!ok) {
                ArrayList<String> tmp = new ArrayList<String>();
                for (int k = 0; k < 8; k++) {
                    tmp.add("-");
                }
                csv.get(i).addAll(1, tmp);
            }
        }

        // 達成度
        int cnt = 0;
        for (var c : csv) {
            List<String> ach = new ArrayList<>();
            double a, b;
            for (int i = 0; i < 8; i++) {
                if (c.get(1 + i).equals("-")) {
                    ach.add("-");
                    continue;
                }
                if (c.get(1 + i).equals("-")) {
                    a = 0.1;
                }
                else a = (double)Integer.parseInt(c.get(1 + i));
                if (c.get(1 + 8 + i).equals("-")) {
                    b = 0;
                }
                else b = (double)Integer.parseInt(c.get(1 + 8 + i));
                if (b / a >= 1.0) {
                    ach.add("☆");
                }
                else if (b / a >= 0.5) {
                    ach.add("◎");
                }
                else {
                    ach.add("〇");
                }
            }
            csv.get(cnt).addAll(ach);
            cnt++;
        }

        // 個人コメント
        String[][] commentBox = {
                {
                    "\"あなたは毎日着実に計画達成に向けて取り組んでいます。\n" +
                            "また、毎日自身の立てた目標・計画通りに勉強をこなしています。\n" +
                            "次の計画でも継続できるように頑張ってください\"",
                    "\"あなたは1週間の半分以上、計画達成に向けて取り組みました。\n" +
                            "1日数問でも解く時間を作ってみたらどうでしょうか？\n" +
                            "継続は力なりです！\"",
                    "\"あなたは１週間に1,2回程度計画達成に向けて取り組みました。\n" +
                            "1日数問から継続して勉強してみませんか？\n" +
                            "継続は力なりです。\"",
                    "1日10分でも計画に向けて取り組んでみよう！"
                },
                {
                    "\"あなたは毎日着実に計画達成に向けて取り組んでいます。\n" +
                            "目標達成までもう少し！\n" +
                            "今週を振り返って達成できる目標を考えてみよう！\"",
                    "\"あなたは1週間の半分以上、計画達成に向けて取り組みました。\n" +
                            "目標達成まであと少し！\n" +
                            "今後を振り返って達成できる目標を考えてみよう！\"",
                    "\"あなたは1週間に1,2回程度計画達成に向けて取り組みました。\n" +
                            "目標達成までもう少し！\n" +
                            "今週を振り返って達成できる目標を考えてみよう！\"",
                    "1日10分でも計画に向けて取り組んでみよう！"
                },
                {
                    "\"あなたは毎日着実に計画達成に向けて取り組んでいます。\n" +
                            "目標達成にはもう少し工夫が必要かも！\n" +
                            "今週を振り返って達成できる目標を考えてみよう！\"",
                    "\"あなたは１週間の半分以上、計画達成に向けて取り組みました。\n" +
                            "目標達成にはもう少し工夫が必要かも！\n" +
                            "今週を振り返って達成できる目標を考えてみよう！\"",
                    "\"あなたは１週間に1,2回程度計画達成に向けて取り組みました。\n" +
                            "目標達成にはもう少し工夫が必要かも！\n" +
                            "今週を振り返って達成できる目標を考えてみよう！\"",
                    "1日10分でも計画に向けて取り組んでみよう！"
                },
                {
                    "1日10分でも計画に向けて取り組んでみよう！",
                    "1日10分でも計画に向けて取り組んでみよう！",
                    "1日10分でも計画に向けて取り組んでみよう！",
                    "1日10分でも計画に向けて取り組んでみよう！"
                }
        };

        cnt = 0;
        for (var c : csv) {
            int[] count = {0, 0, 0, 0};
            for (int i = 0; i < 7; i++) {
                if (c.get(18 + i).equals("-")) {
                    count[0]++;
                }
                else if (c.get(18 + i).equals("☆")) {
                    count[1]++;
                }
                else if (c.get(18 + i).equals("◎")) {
                    count[2]++;
                }
                else if (c.get(18 + i).equals("〇")) {
                    count[3]++;
                }
            }
            int sum = count[1] + count[2] + count[2];
            int col,row;

            if (sum >= 6) row = 0;
            else if (sum >= 3) row = 1;
            else if (sum >= 1) row = 2;
            else row = 3;
            if (count[1] >= count[2] && count[1] >= count[3]) col = 0;
            else if (count[2] >= count[3]) col = 1;
            else col = 2;
            if (sum == 0) col = 3;
            if (csv.get(cnt).get(15).equals("-")) {
                csv.get(cnt).add("-");
            }
            else csv.get(cnt).add(commentBox[col][row]);
            cnt++;
        }

        // 全体コメントと☆
        commentBox = new String[][]{{
                "\"あなたはeラーニング活用期間に参加している生徒の中で、\n" +
                        "他の人よりもとてもたくさん問題を解きました！\n" +
                        "今週もその頑張りを続けてください！\"",
                "\"あなたはeラーニング活用期間に参加している生徒の中で、\n" +
                        "多く問題を解きました！\n" +
                        "今週もその頑張りを続けてください！\"",
                "\"あなたの周りにはもっと多くの問題を解いている生徒もいます。\n" +
                        "もっと多くの問題に取り組んでみよう！\"",
        }};

        Score qCount = new Score();
        for (var c : csv) {
            qCount.addScore((double)Integer.parseInt(c.get(16)));
        }
        double mean = qCount.mean();
        double std = qCount.std();

        for (int i = 0; i < csv.size(); i++) {
            double score = (double)Integer.parseInt(csv.get(i).get(16));
            if (csv.get(i).get(15).equals("-")) {
                csv.get(i).add("-");
                csv.get(i).add("-");
                continue;
            }
            if(mean + std < score) {
                csv.get(i).add(commentBox[0][0]);
                csv.get(i).add("★★★");
            }
            else if (mean < score) {
                csv.get(i).add(commentBox[0][1]);
                csv.get(i).add("★★");
            }
            else {
                csv.get(i).add(commentBox[0][2]);
                csv.get(i).add("★");
            }
        }

        // 達成した個数
        cnt = 0;
        for (var c : csv) {
            List<String> ach = new ArrayList<>();
            double a, b;
            int cnta = 0, cntb = 0, cntc = 0;
            for (int i = 0; i < 8; i++) {
                if (c.get(1 + i).equals("-")) {
                    continue;
                }

                if (c.get(1 + i).equals("-")) {
                    a = 0.1;
                }
                else a = (double)Integer.parseInt(c.get(1 + i));
                if (c.get(1 + 8 + i).equals("-")) {
                    b = 0;
                }
                else b = (double)Integer.parseInt(c.get(1 + 8 + i));
                if (b / a >= 1.0) {
                    cnta++;
                }
                else if (b / a >= 0.5) {
                    cntb++;
                }
                else {
                    cntc++;
                }
            }
            ach.add(String.valueOf(cnta));
            ach.add(String.valueOf(cntb));
            ach.add(String.valueOf(cntc));
            csv.get(cnt).addAll(ach);
            cnt++;
        }

        // ラベルを用意
        ArrayList<String> label = new ArrayList<>();
        label.add("アカウント名");
        label.add("目標-月");label.add("目標-火");label.add("目標-水");
        label.add("目標-木");label.add("目標-金");label.add("目標-土");
        label.add("目標-日");label.add("目標-合計");
        label.add("結果-月");label.add("結果-火");label.add("結果-水");
        label.add("結果-木");label.add("結果-金");label.add("結果-土");
        label.add("結果-日");label.add("結果-合計");
        label.add("ベロシティ");
        label.add("達成度-月");label.add("達成度-火");label.add("達成度-水");
        label.add("達成度-木");label.add("達成度-金");label.add("達成度-土");
        label.add("達成度-日");label.add("達成度-合計");
        label.add("コメント(個人)");label.add("コメント(全体)");label.add("★の数");
        label.add("☆の数");label.add("◎の数");label.add("〇の数");

        csv.add(0, label);

        // Stringをcsvとして書き込む
        String text = "";
        for (var c : csv) {
            System.out.println(c);
            text += getCsvString(c);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(timestamp);
        file3[0] = new File(str + ".csv");
        try (PrintWriter pw = new PrintWriter(file3[0], Charset.forName("Shift-JIS"))) {
            pw.println(text);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public static String getCsvString(List<String> strings) {
        var text = String.join(",", strings);
        text += "\r\n";
        return text;
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

class Score {
    // 変数の宣言
    public static List<Person> person = new ArrayList<>();
    private static double mean;
    private static double std;

    // 計算済みのフラグ
    private static boolean mean_Calculated = false;
    private static boolean std_Calculated = false;

    // 点数, 標準偏差を格納するクラスを定義
    class Person {
        double score;  // 点数
        double Tscore; // 偏差値

        // 数値が一つだけ渡されたときは score として扱う
        Person(double i) {
            score = i;
            mean_Calculated ^= mean_Calculated;  // 平均値の再計算要
        }
    }

    // 点数を追加
    public void addScore(double i) {
        person.add(new Person(i));
    }

    // 平均を求めて返す
    public double mean() {
        if (mean_Calculated) return mean; // 計算済みの平均を返す
        mean = 0.0;
        for (Person P : person) {
            mean += P.score;
        }
        mean /= person.size();
        mean_Calculated = true; // 平均値は計算済み
        std_Calculated ^= std_Calculated; // 標準偏差の再計算要
        return mean;
    }

    // 標準偏差を求めて返す ※事前に平均が求められていること
    public double std() {
        if (!mean_Calculated) mean(); // 平均値を計算していなければ計算する
        if (std_Calculated) return std; // 計算済みの標準偏差を返す
        std = 0.0;
        for (Person P : person) {
            std += Math.pow((P.score - mean), 2.0);
        }
        std = Math.sqrt(std/person.size());
        std_Calculated = true; // 標準偏差は計算済み
        return std;
    }

    // 各人の偏差値を求める ※事前に平均と標準偏差が求められていること
    public void updateTscore() {
        if (!mean_Calculated) mean(); // 平均値を計算していなければ計算する
        if (!std_Calculated) std(); // 標準偏差を計算していなければ計算する
        for (Person P : person) {
            P.Tscore = (P.score - mean) / std * 10 + 50;
        }
    }
}