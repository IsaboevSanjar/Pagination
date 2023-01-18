package uz.sanjar.pagination

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import uz.sanjar.pagination.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ResponseRecyclerView.RecyclerScrollListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val adapter = DataAdapter()
    private val layoutManager = GridLayoutManager(this, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = layoutManager
        workingWithData()

        savedInstanceState?.let {
            if (it.containsKey("lastComplete")) {
                val lastVisibleItem = it.getInt("last", 0)
                //binding.dataList.scrollToPosition(lastVisibleItem)
                binding.dataList.smoothScrollToPosition(lastVisibleItem)
            }
        }

        binding.dataList.enablePagination(true)
        binding.dataList.setScrollListener(this)
    }

    private fun workingWithData() {
        val listener =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            adapter.notifyItemRemoved(viewHolder.adapterPosition)
                            Toast.makeText(this@MainActivity, "Left", Toast.LENGTH_SHORT).show()
                        }
                        ItemTouchHelper.RIGHT -> {
                            Toast.makeText(this@MainActivity, "Right", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        val itemTouchHelper = ItemTouchHelper(listener)
        itemTouchHelper.attachToRecyclerView(binding.dataList)

        binding.dataList.setColumnCount(1)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

        outState.putInt("lastComplete", lastVisibleItemPosition)
    }

    override fun loadRecyclerData(page: Int) {
        Toast.makeText(this, "Page end $page", Toast.LENGTH_SHORT).show()

        adapter.COUNT += 20
        adapter.notifyItemRangeInserted(adapter.COUNT - 20, 20)
    }

    override fun showFab() {
        binding.fab.show()
    }

    override fun hideFab() {
        binding.fab.hide()
    }
}