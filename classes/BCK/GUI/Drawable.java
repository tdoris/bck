package BCK.GUI;

public interface Drawable {
   BCKImageControl Draw();

   boolean isSelected();

   void setSelected(boolean var1);

   void setVisible(boolean var1);

   BCKImageControl getImage();

   void trainPressed();

   void editPressed();

   void classifyPressed();

   void cutPressed();
}
