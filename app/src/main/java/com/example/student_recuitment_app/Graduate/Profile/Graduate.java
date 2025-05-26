package com.example.student_recuitment_app.Graduate.Profile;

public class Graduate {

    private int id;

    private String name;
    private String email;
    private String password;
    private String grad_loc;
    private String grad_degree;

    private byte[] grad_file;
    private byte[] grad_image;
    private String grad_skills;
    private String grad_bio;

    public Graduate(int id, String name, String email, String password, String grad_loc, String grad_degree, byte[] grad_file, byte[] grad_image, String grad_skills, String grad_bio) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.grad_loc = grad_loc;
        this.grad_degree = grad_degree;
        this.grad_file = grad_file;
        this.grad_image = grad_image;
        this.grad_skills = grad_skills;
        this.grad_bio = grad_bio;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getGrad_loc() {
        return grad_loc;
    }
    public String getGrad_degree() {
        return grad_degree;
    }
    public byte[] getGrad_file() {
        return grad_file;
    }
    public byte[] getGrad_image() {
        return grad_image;
        }
    public String getGrad_skills() {
        return grad_skills;
    }
    public String getGrad_bio() {
        return grad_bio;
    }
    
}
