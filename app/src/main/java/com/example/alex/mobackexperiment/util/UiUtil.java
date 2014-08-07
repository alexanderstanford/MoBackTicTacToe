package com.example.alex.mobackexperiment.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by marks on 8/7/14.
 */
public class UiUtil {

    //
    // Sets the orientation for a particular activity based on whether it's
    // a table or phone.
    //
    public static void setOrientation(Activity activity) {
        boolean isTablet = UiUtil.isTabletDevice(activity);
        if (isTablet) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static boolean isTabletDevice(Context activityContext) {
        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

                // Yes, this is a tablet!
                return true;
            }
        }

        // No, this is not a tablet!
        return false;
    }

    public static Point getDisplayDimensions(Activity activity) {
        Point point = new Point();

        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getSize(point);

        return point;
    }

    public static int getDisplayWidth(Activity activity) {
        Point mPoint = getDisplayDimensions(activity);
        int width = mPoint.x;
        return width;
    }

    public static int getDisplayHeight(Activity activity) {
        Point mPoint = getDisplayDimensions(activity);
        int height = mPoint.y;
        return height;
    }

    public static int getParentWidth(View parent){
        return parent.getWidth();
    }

    public static int getParentHeight(View parent){
        return parent.getHeight();
    }

    /**
     * Put elipsicesizes on a string if greater than max, if less than max add
     * spaces to even them all out
     *
     * @param input
     * @param maxLength
     * @return String with Elipsicizes
     * @author Mark Stanford
     */
    public static String ellipsize(String input, int maxLength) {
        if (input == null || input.length() < maxLength) {
            String spaces = "";
            for (int i = 0; i < input.length() - maxLength; i++)
                spaces = spaces + " ";
            return input + spaces;
        }
        return input.substring(0, maxLength - 2) + "...";
    }

    /**
     * Listener for gestures.
     *
     * Recursively looks into the children of the view for which view contains
     * the gesture.
     *
     */
    public static View findViewAtPosition(View parent, int x, int y) {

        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                View viewAtPosition = findViewAtPosition(child, x, y);
                if (viewAtPosition != null) {
                    return viewAtPosition;
                }
            }
            return null;
        } else {
            Rect rect = new Rect();
            parent.getGlobalVisibleRect(rect);
            if (rect.contains(x, y)) {
                return parent;
            } else {
                return null;
            }
        }
    }

    /**
     * Parses the attr set from an xml resource.
     * Useful for copying the attr of a resource and using it to prgrammatically
     * 	create similar resources by putting the attrSet in the constructor
     * @param ResourceID
     * @param activity
     * @return
     */
    public static AttributeSet parseAttrSet(int ResourceID,Activity activity){
        AttributeSet as = null;
        Resources r = activity.getResources();
        XmlResourceParser parser = r.getLayout(ResourceID);

        int state = 0;
        do {
            try {
                state = parser.next();
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (state == XmlPullParser.START_TAG) {
                if (parser.getName().equals("TextView")) {
                    as = Xml.asAttributeSet(parser);
                    break;
                }
            }
        } while(state != XmlPullParser.END_DOCUMENT);

        return as;
    }

    /**
     * Recursively finds the position in a relative view.
     * @param myView
     * @return
     */
    public int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    /**
     * Recursively finds the position in a relative view.
     * @param myView
     * @return
     */
    public int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }
}
