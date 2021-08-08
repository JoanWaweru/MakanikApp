//package com.joan.makanikapp;
//
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DrawerAdapter extends RecyclerView.Adapter <DrawerAdapter.ViewHolder>{
//
//    private List<DrawerItem<ViewHolder>> items;
//    private Map<Class<? extends DrawerItem>,Integer> viewTypes;
//    private SparseArray<DrawerItem<ViewHolder>> holderFactories;
//
//    private OnItemSelectedListener listener;
//
//    private DrawerAdapter(List<DrawerItem<ViewHolder>> items){
//        this.items=items;
//        this.viewTypes=new HashMap<>();
//        this.holderFactories=new SparseArray<>();
//        processViewTypes();
//    }
//
//    private void processViewTypes() {
//        int type = 0;
//        for(DrawerItem item : items){
//            if(!viewTypes.containsKey(item.getClass())){
//                viewTypes.put(item.getClass(),type);
//                holderFactories.put(type,item);
//                type++;
//            }
//        }
//    }
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ViewHolder holder = holderFactories.get(viewType).createViewHolder(parent);
//        holder.drawerAdapter = this;
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DrawerAdapter.ViewHolder holder, int position) {
//        items.get(position).bindViewHolder(holder);
//    }
//
//    private int getViewType(int position){
//        return viewTypes.get(items.get(position).getClass());
//    }
//
//    public void setSelected(int position){
//        DrawerItem newChecked = items.get(position);
//        if(!newChecked.isSelectable()){
//            return;
//        }
//        for (int i=0;i<items.size();i++){
//            DrawerItem item = items.get(i);
//            if(item.isChecked()){
//                item.setChecked(false);
//                notifyItemChanged(i);
//                break;
//            }
//        }
//
//        newChecked.setChecked(true);
//        notifyItemChanged(position);
//
//        if(listener != null){
//            listener.onItemSelected(position);
//        }
//    }
//
//    public void setListener(AdapterView.OnItemSelectedListener listener){
//        this.listener = (OnItemSelectedListener) listener;
//    }
//
//    public interface OnItemSelectedListener{
//        void onItemSelected(int position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    static abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        private DrawerAdapter drawerAdapter;
//        public ViewHolder(@NonNull View itemView){
//            super(itemView);
//            itemView.setOnClickListener(this);
//        }
//        @Override
//        public void onClick(View v){
//            drawerAdapter.setSelected(getAdapterPosition());
//        }
//    }
//}