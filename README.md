# quicklib
Android Data Binding、ViewModel 与 Material design开发库

## Data Binding：
* ListBindingAdapter
* BindingFragment
* BindingActivity
* BindingMaterialActivity
## Data Binding与ViewModel:
* VMBindingActivity
* VMBindingMaterialActivity
* VMBindingFragment
## 使用示例：
```
//Sample class
public class ListFragment extends VMBindingFragment<ServiceViewModel, FragmentListBinding> {


    public static Fragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BindingUtil.setVerticalLayoutManager(viewBinding.recycleView);
        viewBinding.recycleView.setAdapter(new UserListAdapter());
        viewModel.getRemote1UserList();
    }

    @Override
    protected boolean shouldShareViewModel() {
        return true;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_list;
    }

    @Override
    public Class<ServiceViewModel> provideViewModelClass() {
        return ServiceViewModel.class;
    }


    private class UserListAdapter extends ListBindingAdapter<UserItemBinding> {

        @Override
        protected int layoutRes() {
            return R.layout.user_item;
        }
    }
}
```
## Material design Dialog:
* MaterialAlertDialog
![alt sample](https://github.com/riyol/quicklib/blob/master/Screenshot/Screenshot_20181126-173446.jpg)
