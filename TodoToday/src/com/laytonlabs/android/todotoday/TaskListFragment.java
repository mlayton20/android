package com.laytonlabs.android.todotoday;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends ListFragment {
	ArrayList<Task> mTasks;
	private Callbacks mCallbacks;
	private static final String TAG = "TaskListFragment";
	private static final String DIALOG_SHARE = "share";
	private static final int REQUEST_SHARE = 0;
	
	public interface Callbacks {
		void onTaskSelected(Task task);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crimes_title);
		mTasks = TaskLab.get(getActivity()).getTasks();
		
		TaskAdapter adapter = new TaskAdapter(mTasks);
		setListAdapter(adapter);
		setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View  emptyView = getActivity().getLayoutInflater().inflate(R.layout.fragment_empty_list, null);
		((ViewGroup)getListView().getParent()).addView(emptyView);
		getListView().setEmptyView(emptyView);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);	
		
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			//Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			//Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						TaskAdapter adapter = (TaskAdapter)getListAdapter();
						TaskLab taskLab = TaskLab.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								taskLab.deleteTask(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		TaskLab.get(getActivity()).saveTasks();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task t = ((TaskAdapter)getListAdapter()).getItem(position);
		mCallbacks.onTaskSelected(t);
	}
	
	private class TaskAdapter extends ArrayAdapter<Task> {
		public TaskAdapter(ArrayList<Task> tasks) {
			super(getActivity(), 0, tasks);
		}
		
		private class ViewHolder {
			TextView titleTextView;
			CheckBox completedCheckBox;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			//If we wern't given a view, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task,  null);
				
				holder = new ViewHolder();
				holder.titleTextView = (TextView)convertView.findViewById(R.id.task_list_item_titleTextView);
				holder.completedCheckBox = (CheckBox)convertView.findViewById(R.id.task_list_item_completedCheckBox);
				convertView.setTag(holder);
				
				holder.completedCheckBox.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Task task = (Task) cb.getTag();
						task.setCompleted(cb.isChecked());
						TaskLab.get(getActivity()).move(task);
						updateUI();
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			//Configure the view for this Task
			Task t = getItem(position);
			holder.titleTextView.setText(t.getmTitle());
			holder.completedCheckBox.setChecked(t.isCompleted());
			holder.completedCheckBox.setTag(t);
			
			return convertView;
		}
	}
	
	public void updateUI() {
		((TaskAdapter)getListAdapter()).notifyDataSetChanged();
		
		if (TaskLab.get(getActivity()).allTasksCompleted()) {
			showTasksCompletedDialog();
		}
	}
	
	public void showTasksCompletedDialog() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		TasksCompleteDFragment dialog = new TasksCompleteDFragment();
		dialog.setTargetFragment(TaskListFragment.this, REQUEST_SHARE);
		dialog.show(fm, DIALOG_SHARE);
	}

}
