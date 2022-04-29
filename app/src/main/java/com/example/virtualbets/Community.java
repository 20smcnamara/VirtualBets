package com.example.virtualbets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Community {

    //Owners secret code
    private final int OWNER_ID;

    //Owner Commands
    private final static int ADD_ADMIN = -32;
    private final static int REMOVE_ADMIN = -128;
    private final static int CHANGE_OWNER = -64;

    //Server Commands
    public final static int ADD_MEMBER = 1;
    public final static int REMOVE_MEMBER = ADD_MEMBER + 1;
    private final static int HIGHEST_SERVER_COMMAND = REMOVE_MEMBER;

    //Event Commands
    public final static int CREATE_EVENT = HIGHEST_SERVER_COMMAND + 1;
    public final static int EDIT_EVENT = CREATE_EVENT + 1;
    public final static int DELETE_EVENT = EDIT_EVENT + 1;
    private final static int HIGHEST_EVENT_COMMAND = DELETE_EVENT;

    private final static int HIGHEST_COMMAND = HIGHEST_EVENT_COMMAND;

    private final static HashMap<String, Integer> CommandIndex = new HashMap<String, Integer>() {{
        put("add member", ADD_MEMBER);
        put("add admin", ADD_ADMIN);

        put("remove member", REMOVE_MEMBER);
        put("remove admin", REMOVE_ADMIN);

        put("create event", CREATE_EVENT);
        put("delete event", DELETE_EVENT);
        put("edit event", EDIT_EVENT);

    }};

    private final String code;

    private final String owner;
    private final StringBuilder log = new StringBuilder();

    private final ArrayList<String> users = new ArrayList<>();
    private final ArrayList<String> admin = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();

    public Community(String admin){
        this.admin.add(admin);
        this.users.add(admin);
        this.owner = admin;

        String logMsg = "Created by: " + admin + " at " + System.currentTimeMillis() + "\n";
        log.append(logMsg);


        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        String newCode = "XXXXXX";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++){
            stringBuilder.append(r.nextInt(26) + 65);
            if (i == 5) {
                newCode = stringBuilder.toString();
                stringBuilder = new StringBuilder();
            }
        }

        r.setSeed(System.currentTimeMillis());
        OWNER_ID = r.nextInt();

        code = newCode;
    }

    public String getCode() {
        return code;
    }

    public String getLog() {
        return log.toString();
    }

    // Command format is as follows: .add member
    public boolean parseCommand(String command, String issuer) {
        String toRun = command.substring(1);

        if (command.charAt(0) == '.' && CommandIndex.containsKey(command.substring(1))) {

            if (CommandIndex.containsKey(toRun)) {
                return runStrCommand(toRun, issuer);
            } else {
                recordLog(issuer + " ran unknown command " + toRun);
                return false;
            }
        } else {
            recordLog(issuer + " failed to run " + toRun);
            return false;
        }
    }

    private void recordLog(String msg) {
        String fullMessage = msg + " at time " + System.currentTimeMillis();
        log.append(fullMessage);
    }

    private boolean runStrCommand(String commandStr, String issuer) {
        int command = CommandIndex.get(commandStr);

        if (getUserPermissionLevel(issuer) >= getCommandLevel(command)) {
            switch (command) {
                case ADD_MEMBER:
                    addMember(commandStr);
                    break;

                case REMOVE_MEMBER:
                    removeMember(commandStr);
                    break;

                case REMOVE_ADMIN:
                    removeAdmin(commandStr);
                    break;

                case ADD_ADMIN:
                    addAdmin(commandStr);
                    break;

                case CREATE_EVENT:
                    createEvent(commandStr);

                case EDIT_EVENT:
                    editEvent(commandStr);

                case DELETE_EVENT:
                    deleteEvent(commandStr);

                default:
                    recordLog(issuer + " tried to run non-existent command " + commandStr);
                    return false;
            }

            return true;
        } else {
            recordLog(issuer + " was denied the ability to run " + commandStr);
            return false;
        }
    }

    private void sentAlert(String message) {

    }

    private void deleteEvent(String command) {

    }

    private void editEvent(String command) {

    }

    private void createEvent(String command) {
        //Format ".create event name yyyy mm dd hh mm :desc
        String[] parsed = command.split(" ");
        String Ename = parsed[2];
        int year = Integer.parseInt(parsed[3]);
        int month = Integer.parseInt(parsed[4]);
        int day = Integer.parseInt(parsed[5]);
        int hour = Integer.parseInt(parsed[6]);
        int minute = Integer.parseInt(parsed[7]);
        String Edesc = command.split(":")[8];

        Event newEvent = new Event(Ename, Edesc, year, month, day, hour, minute);

        events.add(newEvent);
    }

    private int getCommandLevel(int command) {
        if (command < 0) {
            return 3;
        } else if (0 < command && command <= HIGHEST_SERVER_COMMAND) {
            return 1;
        } else if (HIGHEST_SERVER_COMMAND < command && command <= HIGHEST_COMMAND) {
            return 1;
        } else {
            return 255;
        }
    }

    private int getUserPermissionLevel(String issuer) {
        if (issuer.equals(owner)) {
            return 2;
        } else if (admin.contains(issuer)) {
            return 1;
        } else {
            return 0;
        }

    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    private void addMember(String user) {
        users.add(user);
    }

    private void removeMember(String user){
        users.remove(user);
    }

    private void addAdmin(String commandStr) {
        admin.add(commandStr);
    }

    private void removeAdmin(String user){
        admin.remove(user);
    }
}
