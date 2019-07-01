package com.dhht.nana.data;

public class Replay{
        String session;
        String answer;

    public Replay(String session, String answer) {
        this.session = session;
        this.answer = answer;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Replay{" +
                "session='" + session + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}