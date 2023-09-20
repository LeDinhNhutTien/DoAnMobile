package TienIch;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class WidgetAdapter extends RecyclerView.Adapter<WidgetAdapter.WidgetViewHolder> {
    private List<Widget> widgetList;

    public WidgetAdapter(List<Widget> widgetList) {
        this.widgetList = widgetList;
    }

    @NonNull
    @Override
    public WidgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget, parent, false);
        return new WidgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetViewHolder holder, int position) {
        Widget widget = widgetList.get(position);

        holder.widgetNameTextView.setText(widget.getName());
        holder.widgetIconImageView.setImageResource(widget.getIconResId());

        holder.widgetIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy màn hình tương ứng từ đối tượng Widget
                Class<?> destinationClass = widget.getDestinationClass();
                if (destinationClass != null) {
                    Intent intent = new Intent(v.getContext(), destinationClass);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return widgetList.size();
    }

    public static class WidgetViewHolder extends RecyclerView.ViewHolder {
        public ImageView widgetIconImageView;
        public TextView widgetNameTextView;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            widgetIconImageView = itemView.findViewById(R.id.widgetIconImageView);
            widgetNameTextView = itemView.findViewById(R.id.widgetNameTextView);
        }
    }
}

