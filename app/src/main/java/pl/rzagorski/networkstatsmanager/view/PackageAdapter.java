package pl.rzagorski.networkstatsmanager.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.rzagorski.networkstatsmanager.R;
import pl.rzagorski.networkstatsmanager.model.Package;
import pl.rzagorski.networkstatsmanager.utils.OnPackageClickListener;

/**
 * Created by Robert Zagórski on 2016-12-14.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    List<Package> mPackageList;
    OnPackageClickListener mListener;

    public PackageAdapter(List<Package> packageList, OnPackageClickListener listener) {
        mPackageList = packageList;
        mListener = listener;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_card, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
        Package packageItem = mPackageList.get(position);
        holder.name.setText(packageItem.getName());
        holder.packageName.setText(packageItem.getPackageName());
        holder.version.setText(packageItem.getVersion());
        try {
            holder.icon.setImageDrawable(holder.context.getPackageManager().getApplicationIcon(packageItem.getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mPackageList.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView name;
        TextView packageName;
        TextView version;
        AppCompatImageView icon;


        public PackageViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.name);
            packageName = (TextView) itemView.findViewById(R.id.package_name);
            version = (TextView) itemView.findViewById(R.id.version);
            icon = (AppCompatImageView) itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PackageAdapter.this.mListener.onClick(mPackageList.get(getAdapterPosition()));
                }
            });
        }
    }
}
