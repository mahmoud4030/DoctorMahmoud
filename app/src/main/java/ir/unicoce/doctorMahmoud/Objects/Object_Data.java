package ir.unicoce.doctorMahmoud.Objects;

/**
 * Created by soheil syetem on 11/26/2016.
 */

public class Object_Data {

    public int sid,parentId;
    public String title,content,imageUrl;
    public boolean favoirite ;

    public Object_Data(int sid, int parentId, String title, String content, String imageUrl, boolean favoirite) {
        this.sid = sid;
        this.parentId = parentId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.favoirite = favoirite;
    }

    public int getSid() {
        return sid;
    }

    public int getParentId() {
        return parentId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavoirite() {
        return favoirite;
    }
}