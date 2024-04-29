package BCK.GUI;

import com.sun.java.swing.JEditorPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import com.sun.java.swing.JViewport;
import com.sun.java.swing.SwingUtilities;
import com.sun.java.swing.border.EmptyBorder;
import com.sun.java.swing.border.SoftBevelBorder;
import com.sun.java.swing.event.HyperlinkEvent;
import com.sun.java.swing.event.HyperlinkListener;
import com.sun.java.swing.event.HyperlinkEvent.EventType;
import com.sun.java.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HtmlPanel extends JPanel implements HyperlinkListener {
   JEditorPane html;

   public HtmlPanel() {
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.setLayout(new BorderLayout());
      this.getAccessibleContext().setAccessibleName("HTML panel");
      this.getAccessibleContext().setAccessibleDescription("A panel for viewing HTML documents, and following their links");

      try {
         URL var1 = new URL("file:" + bckframe.home + "Help/help.html");
         this.html = new JEditorPane(var1);
         this.html.setEditable(false);
         this.html.addHyperlinkListener(this);
         JScrollPane var2 = new JScrollPane();
         var2.setBorder(new SoftBevelBorder(1));
         JViewport var3 = var2.getViewport();
         var3.add(this.html);
         var3.setBackingStoreEnabled(true);
         this.add(var2, "Center");
      } catch (MalformedURLException var4) {
         System.out.println("Malformed URL: " + var4);
      } catch (IOException var5) {
         System.out.println("IOException: " + var5);
      }
   }

   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if (var1.getEventType() == EventType.ACTIVATED) {
         this.linkActivated(var1.getURL());
      }
   }

   protected void linkActivated(URL var1) {
      Cursor var2 = this.html.getCursor();
      Cursor var3 = Cursor.getPredefinedCursor(3);
      this.html.setCursor(var3);
      SwingUtilities.invokeLater(new HtmlPanel.PageLoader(var1, var2));
   }

   class PageLoader implements Runnable {
      URL url;
      Cursor cursor;

      PageLoader(URL var2, Cursor var3) {
         this.url = var2;
         this.cursor = var3;
      }

      public void run() {
         if (this.url == null) {
            HtmlPanel.this.html.setCursor(this.cursor);
            Container var8 = HtmlPanel.this.html.getParent();
            var8.repaint();
         } else {
            Document var1 = HtmlPanel.this.html.getDocument();

            try {
               HtmlPanel.this.html.setPage(this.url);
            } catch (IOException var6) {
               HtmlPanel.this.html.setDocument(var1);
               HtmlPanel.this.getToolkit().beep();
            } finally {
               this.url = null;
               SwingUtilities.invokeLater(this);
            }
         }
      }
   }
}
