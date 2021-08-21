package italo.gerimp;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.JobAttributes;
import java.awt.JobAttributes.DialogType;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GerImp extends JPanel {

	private static final long serialVersionUID = 1L;
		
	public GerImp() {
		PrintService[] psvet = PrintServiceLookup.lookupPrintServices( DocFlavor.INPUT_STREAM.AUTOSENSE, null );
		
		JobAttributes attr = new JobAttributes();
		attr.setDialog( DialogType.NONE );
		attr.setPrinter( psvet[ 1 ].getName() );
		
		
		PageAttributes pa = new PageAttributes();
		
		PrintJob job = Toolkit.getDefaultToolkit().getPrintJob( new JFrame(), "Impressora PDF", attr, pa );
		//PrintService ps = ServiceUI.printDialog( null, 200, 200, psvet, defaultPS, DocFlavor.INPUT_STREAM.AUTOSENSE, pras );
		Graphics g = job.getGraphics();
		
		String str = "Alô Mundo!";
		g.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 24 ) );
		Rectangle2D r = g.getFontMetrics().getStringBounds( str, g );
		
		int w = job.getPageDimension().width;
		int h = job.getPageDimension().height;
		
		int x = ( w - (int)r.getWidth() ) / 2;
		int y = ( h - (int)r.getHeight() ) / 2 + (int)r.getHeight() / 2;
		g.drawString( str, x, y );
		
		g.dispose();
		job.end();
		/*
		try {			
			DocPrintJob job = psvet[2].createPrintJob();						
			job.addPrintJobListener( this );
			
			InputStream in = new ByteArrayInputStream( "Teste!".getBytes() );
			SimpleDoc doc = new SimpleDoc( in, DocFlavor.INPUT_STREAM.AUTOSENSE, null );
			
			job.print( doc, null );	
			
			while( imprimindo ) {
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					
				}
			}
			
			in.close();
		} catch (PrintException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/			
	}
	
	public void paintComponent( Graphics g ) {
		super.paintComponent(g);
		String str = "Alô mundo!";

		g.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 24 ) );
		Rectangle2D r = g.getFontMetrics().getStringBounds( str, g );
		
		int w = super.getWidth();
		int h = super.getHeight();
		
		int x = ( w - (int)r.getWidth() ) / 2;
		int y = ( h - (int)r.getHeight() ) / 2 + (int)r.getHeight() / 2;
		g.drawString( str, x, y );		
	}
	
	public static void main(String[] args) {
		JPanel p;
		JFrame f = new JFrame();
		f.setContentPane( new GerImp() ); 
		f.setTitle( "Teste JNLP" );
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setSize( 300, 200 );
		f.setLocationRelativeTo( f );
		f.setVisible( true ); 
		
		/*
		PrintJob job = p.getToolkit().getPrintJob( f, "Teste de impressão.", null );
		p.paintAll( job.getGraphics() ); 
		job.end();
		*/
	}
	
}
