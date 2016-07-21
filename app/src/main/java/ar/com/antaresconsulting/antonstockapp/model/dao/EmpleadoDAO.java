package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import ar.com.antaresconsulting.antonstockapp.model.Empleado;

import com.openerp.ReadAsyncTask;

public class EmpleadoDAO extends ReadAsyncTask {


	private Activity activityPart;

	public interface EmpleCallbacks {
		void setEmpleados();

	}

	public EmpleadoDAO(Activity frag) {
		super(frag);
		this.activityPart = frag;
		this.mModel = "hr.employee";
		this.mFields = new String[] { "id", "name", "image_small","department_id","job_id"};
	}

	public void getAll() {
		this.setmFilters(new Object[0]);
		this.execute(this.mFields);
	}
	
	@Override
	public void dataFetched() {
			((EmpleCallbacks) this.activityPart).setEmpleados();
	}

	public List<Empleado> getEmpleList() {
		List<Empleado> datosProds = new ArrayList<Empleado>();
		int i = 0 ;
		for (HashMap<String, Object> obj : this.mData) {
			Empleado resp = new Empleado();
			Integer id = obj.get("id") instanceof Boolean ? new Integer(0): (Integer) obj.get("id");
			String image = obj.get("image_small") instanceof Boolean ? "": (String) obj.get("image_small");
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			Object[] department_id = obj.get("department_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("department_id");
			Object[] job_id = obj.get("job_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("job_id");

			resp.setId(id);
			resp.setNombre(nombre);
			resp.setImagen(image);
			resp.setCargo(job_id);
			resp.setDepartamento(department_id);
			datosProds.add(resp);
			i++;
		}
		return datosProds;

	}	
}
