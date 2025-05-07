import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackr.R
import com.example.trackr.model.Transaction


class TransactionAdapter(
    private var transactions: MutableList<Transaction>,
    private val onDeleteClick: (Transaction) -> Unit,
    private val onEditClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val amount = itemView.findViewById<TextView>(R.id.tvAmount)
        val date = itemView.findViewById<TextView>(R.id.tvDate)
        val category = itemView.findViewById<TextView>(R.id.tvCategory)
        val btnDelete = itemView.findViewById<Button>(R.id.btnDelete)
        val btnEdit = itemView.findViewById<Button>(R.id.updatebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.title.text = transaction.title
        holder.amount.text = "Rs. ${transaction.amount}"
        holder.date.text = transaction.date
        holder.category.text = transaction.category
        holder.btnDelete.setOnClickListener { onDeleteClick(transaction) }
        holder.btnEdit.setOnClickListener { onEditClick(transaction) }
    }

    override fun getItemCount() = transactions.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Transaction>) {
        // Always create a new list reference
        transactions = newList.toList().toMutableList() // Create immutable copy
        notifyDataSetChanged() // Full refresh is safer in this case
    }

    fun removeTransaction(position: Int) {
        transactions.removeAt(position)
        notifyItemRemoved(position)
        if (position < transactions.size) {
            notifyItemRangeChanged(position, transactions.size - position)
        }
    }
}
