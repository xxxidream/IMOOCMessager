package net.qiujuer.italker.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;

import net.qiujuer.italker.factory.utils.DiffUiDataCallback;

/**
 * Created by admin on 2017/9/5.
 */

public abstract  class BaseDbModel<Model>  extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model>{

}
