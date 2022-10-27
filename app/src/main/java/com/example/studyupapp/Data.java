package com.example.studyupapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Data {

    private static ArrayList<String> loader = new ArrayList<>();

    public static final String USER_PROFILE_TXT = "user_profile.txt";
    public static final String GROUPS_TXT = "groups.txt";
    public static final String FLASHCARD_SETS_TXT = "flashcard_sets.txt";
    public static final String NOTES_TXT = "notes.txt";
    public static final String PERSONAL_NOTEBOOK_TXT = "personal_notebook.txt";
    public static final String SCHOOL_NOTEBOOK_TXT = "school_notebook.txt";
    public static final String OTHER_NOTEBOOK_TXT = "other_notebook.txt";
    public static final String REMINDERS_TXT = "reminders.txt";

    public static void saveData(String[] data, boolean clear, Context context, String file) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(file, MODE_PRIVATE);
            if(!clear) {
                for(String info: data) {
                    if(info == data[0]) fos.write(info.getBytes());
                    else {
                        fos.write(("\n" + info).getBytes());
                    }
                }
            } else {
                fos.write("".getBytes()); //if clearing
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveData(boolean clear, Context context, String file) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(file, MODE_PRIVATE);

            if(clear) fos.write("".getBytes()); //if clearing


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadData(Context context, String[] destination, String file) {
        FileInputStream fis = null;
        loader.clear();
        try {
            fis = context.openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                loader.add(text); //loads the data into a destination array list that is passed in to be used
            }
            destination = loader.toArray(destination);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void loadData(Context context, ArrayList<String> destination, String file) {
        FileInputStream fis = null;
        loader.clear();
        try {
            fis = context.openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                loader.add(text); //loads the data into a destination array list that is passed in to be used
            }
            destination.clear();
            destination.addAll(loader);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String loadString(Context context, String file) {
        FileInputStream fis = null;
        StringBuilder output = new StringBuilder();
        try {
            fis = context.openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            if((text = br.readLine()) != null) {
                output.append(text); //first line is just appended
            }
            while ((text = br.readLine()) != null) {
                output.append("\n" + text); //second and onward is with new line
            }
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static void appendData(String data, Context context, String file, boolean appendAtEnd) {
        ArrayList<String> existingInfo = new ArrayList<>();
        loadData(context, existingInfo, file);
        if(!appendAtEnd) {
            existingInfo.add(0, data); //appends at start, cause group display priority
        } else {
            existingInfo.add(data); //appends at ent
        }
        saveData(existingInfo.toArray(new String[existingInfo.size()]), false, context, file);
    }


}


