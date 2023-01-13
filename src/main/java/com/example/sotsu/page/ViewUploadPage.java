package com.example.sotsu.page;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Bytes;
import org.springframework.ui.Model;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@MountPath("ViewUpload")
public class ViewUploadPage extends WebPage {

    private FileUploadField uploadField;
    private File uploadDir;
    private File[] file;

    public ViewUploadPage() {
        // Formを拡張したUpload用フォーム
        add(new ViewUploadPage.UploadForm("uploadForm"));

        // アップロードディレクトリからファイルのリスト（配列）を取得
        file = uploadDir.listFiles();
    }

    private File getDirectory(String dirName) {
        WebApplication application = (WebApplication) getApplication();
        // 絶対パスの取得　保存などの操作で相対パスは、Webアプリケーションでは危険
        String uploadPath = application.getServletContext().getRealPath(dirName);
        File dir = new File(uploadPath);

        if (!dir.exists())
            dir.mkdir();

        return dir;
    }

    private class UploadForm extends Form<Model> {
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

            add(new Button("uploadButton") {
                @Override
                public void onSubmit() {
                    FileUpload upload = uploadField.getFileUpload();
                    if (upload == null) {
                        // FeedbackPanelコンポーネントを使用する場合のメッセージ
                        error("アップロードするファイルが見つかりません");
                        return;
                    }

                    String fileName = upload.getClientFileName();
                    // 空ファイルをアップロードするディレクトリへ作成
                    File uploadFile = new File(uploadDir, fileName);

                    try {
                        // 空ファイルへアップロードファイルを書き出す
                        upload.writeTo(uploadFile);
                    } catch (Exception e) {
                        this.error(e.getMessage());
                    } finally {
                        upload.closeStreams();
                    }
                    file = uploadDir.listFiles();
                    try {
                        setResponsePage(new ViewPage(file[0], 0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
