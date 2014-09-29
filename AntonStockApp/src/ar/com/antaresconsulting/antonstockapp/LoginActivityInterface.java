package ar.com.antaresconsulting.antonstockapp;

public interface LoginActivityInterface {
	public void connectionResolved(Boolean result);
	public void dbList(Object[] dbs);

    void setActivities();
}
