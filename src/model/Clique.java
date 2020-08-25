package model;

import java.util.Set;

public class Clique {
	private int degree;
	private int size;
	private Set<Integer> member;
	
	public Clique(int degree, Set<Integer> member) {
		super();
		this.degree = degree;
		this.member = member;
		this.size = member.size();
	}
	
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Set<Integer> getMember() {
		return member;
	}
	public void setMember(Set<Integer> member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "Clique [degree=" + degree + ", size=" + size + ", member=" + member + "]";
	}
	
}
