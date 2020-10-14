package ca.qc.bdeb.graphics;

import ca.qc.bdeb.events.DropEventListener;
import ca.qc.bdeb.events.DropEventHandler;
import ca.qc.bdeb.events.DropEvent;
import ca.qc.bdeb.events.DragEventListener;
import ca.qc.bdeb.events.DragEventHandler;
import ca.qc.bdeb.events.DragEvent;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.EventListenerList;

/**
 * 
 * @author Eric Wenaas
 */

public abstract class DragSprite extends Sprite implements DropEventHandler,
                                                           DragEventHandler
{

    private boolean dragable;
    private boolean dragActive;
    private EventListenerList listeners;
    private DragListener dragListener;

    private Point startingLocation;

    public DragSprite()
    {
        super();
        dragListener = new DragListener(this);
        dragActive = false;
        dragable = true;
        addMouseMotionListener(dragListener);
        addMouseListener(dragListener);
        listeners = new EventListenerList();
    }

    public void setDragable(boolean value)
    {
        dragable = value;
    }

    public boolean isDragable()
    {
        return dragable;
    }

    private void clipInMiddle(Point p)
    {
        int x = p.x + getX() - getWidth() / 2;
        int y = p.y + getY() - getHeight() / 2;
        setLocation(x, y);
        repaint();
    }

    public Point getStartingLocation()
    {
        return startingLocation;
    }

    public void startDrag()
    {
        dragActive = true;
        startingLocation = getLocation();
    }
    
    @Override 
    public void addDragEventListener(DragEventListener listener) {
        listeners.add(DragEventListener.class, listener);
    }
            
    @Override 
    public void removeDragEventListener(DragEventListener listener) {
        listeners.add(DragEventListener.class, listener);        
    }

    @Override
    public void addDropEventListener(DropEventListener listener)
    {
        listeners.add(DropEventListener.class, listener);
    }

    @Override
    public void removeDropEventListener(DropEventListener listener)
    {
        listeners.remove(DropEventListener.class, listener);
    }

    private void fireDropEvent(DropEvent evt)
    {
        Object[] myListeners = listeners.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < myListeners.length; i += 2)
        {
            if (myListeners[i] == DropEventListener.class)
            {
                ((DropEventListener) myListeners[i + 1]).dropEventHappened(evt);
            }
        }
    }
    
    private void fireDragEvent(DragEvent evt) {
        Object[] myListeners = listeners.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < myListeners.length; i += 2)
        {
            if (myListeners[i] == DragEventListener.class)
            {
                ((DragEventListener) myListeners[i + 1]).startDragEventHappened(evt);
            }
        }
        
    }

    private static class DragListener implements MouseMotionListener,
            MouseListener
    {

        private DragSprite instance;

        public DragListener(DragSprite instance)
        {
            this.instance = instance;
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if (instance.dragActive)
            {
                instance.clipInMiddle(e.getPoint());
            }
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            if (instance.isDragable())
            {
                instance.startDrag();
                instance.fireDragEvent(new DragEvent(instance));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (instance.dragActive)
            {
                instance.fireDropEvent(new DropEvent(instance));
            }
            instance.dragActive = false;
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }
    }
}
