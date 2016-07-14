package yhh.bj4.lotterylover.fragments.analyze;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.fragments.analyze.result.AnalyzeResult;
import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/13.
 */
public class AnalyzeResultAdapter extends RecyclerView.Adapter {
    private static final String TAG = "AnalyzeResultAdapter";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String KEY_TYPE = "type";
    public static final String KEY_CATEGORY = "category";

    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT_TYPE = "content_type";

    public static final int TYPE_CATEGORY = 1;
    public static final int TYPE_CONTENT = 2;

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final List<JSONObject> mItems = new ArrayList<>();

    private final AnalyzeResult mResult;

    public AnalyzeResultAdapter(Context context, AnalyzeResult result) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResult = result;
        buildItemList();
    }

    private void buildItemList() {
        final Resources res = mContext.getResources();
        final boolean hasSpecial = LotteryItem.getMaximumSpecialNumber(mResult.getLotteryType()) > 0;
        // all time
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_all_time)));

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5, R.string.analyze_fragment_result_all_time));
        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5, R.string.analyze_fragment_result_all_time));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5_special, R.string.analyze_fragment_result_all_time));
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5_special, R.string.analyze_fragment_result_all_time));
        }

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_longest_not_show, R.string.analyze_fragment_result_all_time));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_longest_not_show_special, R.string.analyze_fragment_result_all_time));
        }
        // 10
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_recent_10)));

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5, R.string.analyze_fragment_result_recent_10));
        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5, R.string.analyze_fragment_result_recent_10));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5_special, R.string.analyze_fragment_result_recent_10));
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5_special, R.string.analyze_fragment_result_recent_10));
        }

        // 20
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_recent_20)));

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5, R.string.analyze_fragment_result_recent_20));
        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5, R.string.analyze_fragment_result_recent_20));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5_special, R.string.analyze_fragment_result_recent_20));
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5_special, R.string.analyze_fragment_result_recent_20));
        }

        // this month
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_this_month)));

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5, R.string.analyze_fragment_result_this_month));
        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5, R.string.analyze_fragment_result_this_month));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5_special, R.string.analyze_fragment_result_this_month));
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5_special, R.string.analyze_fragment_result_this_month));
        }

        // last month
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_last_month)));

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5, R.string.analyze_fragment_result_last_month));
        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5, R.string.analyze_fragment_result_last_month));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5_special, R.string.analyze_fragment_result_last_month));
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5_special, R.string.analyze_fragment_result_last_month));
        }

        // last 2 month
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_last_2_month)));

        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5, R.string.analyze_fragment_result_last_2_month));
        mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5, R.string.analyze_fragment_result_last_2_month));
        if (hasSpecial) {
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_top_5_special, R.string.analyze_fragment_result_last_2_month));
            mItems.add(buildTypeContent(mContext, R.string.analyze_fragment_result_item_last_5_special, R.string.analyze_fragment_result_last_2_month));
        }

    }

    private JSONObject buildTypeCategory(String categoryTitle) {
        JSONObject rtn = new JSONObject();
        try {
            rtn.put(KEY_TYPE, TYPE_CATEGORY);
            rtn.put(KEY_CATEGORY, categoryTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    private JSONObject buildTypeContent(Context context, int strRes, int keyCategory) {
        JSONObject rtn = new JSONObject();
        try {
            rtn.put(KEY_TYPE, TYPE_CONTENT);
            rtn.put(KEY_TITLE, context.getResources().getString(strRes));
            rtn.put(KEY_CONTENT_TYPE, strRes);
            rtn.put(KEY_CATEGORY, keyCategory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CATEGORY) {
            return new AnalyzeResultCategoryHolder(mInflater.inflate(R.layout.analyze_fragment_result_category, parent, false));
        } else if (viewType == TYPE_CONTENT) {
            return new AnalyzeResultContentHolder(mInflater.inflate(R.layout.analyze_fragment_result_content, parent, false));
        }
        throw new RuntimeException("unexpected type: " + viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_CATEGORY) {
            bindTypeCategory(holder, position);
        } else if (viewType == TYPE_CONTENT) {
            bindTypeContent(holder, position);
        } else {
            throw new RuntimeException("unexpected type: " + viewType);
        }
    }

    private String getFiveResultsFromList(List<Pair<Integer, Integer>> list) {
        StringBuilder rtn = new StringBuilder();
        for (int i = 0; i < list.size() && i < 5; ++i) {
            rtn.append(String.format("%02d", list.get(i).first)).append(" (").append(list.get(i).second).append(")");
            if (i != 4) {
                rtn.append(", ");
            }
        }
        return rtn.toString();
    }

    private List<Pair<Integer, Integer>> getTypeContentDataByType(int categoryType, int contentType) {
        if (categoryType == R.string.analyze_fragment_result_all_time) {
            if (contentType == R.string.analyze_fragment_result_item_top_5) {
                return mResult.getDrawingTime().getTopNormalDrawingTimesAllPeriod();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5) {
                return mResult.getDrawingTime().getLastNormalDrawingTimesAllPeriod();
            } else if (contentType == R.string.analyze_fragment_result_item_top_5_special) {
                return mResult.getDrawingTime().getTopSpecialDrawingTimesAllPeriod();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5_special) {
                return mResult.getDrawingTime().getLastSpecialDrawingTimesAllPeriod();
            } else if (contentType == R.string.analyze_fragment_result_item_longest_not_show) {
                return mResult.getNotDrawingNumbers().getLongestNormalNotShowList();
            } else if (contentType == R.string.analyze_fragment_result_item_longest_not_show_special) {
                return mResult.getNotDrawingNumbers().getLongestSpecialNotShowList();
            }
        } else if (categoryType == R.string.analyze_fragment_result_recent_10) {
            if (contentType == R.string.analyze_fragment_result_item_top_5) {
                return mResult.getDrawingTime().getTopNormalDrawingTimes10();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5) {
                return mResult.getDrawingTime().getLastNormalDrawingTimes10();
            } else if (contentType == R.string.analyze_fragment_result_item_top_5_special) {
                return mResult.getDrawingTime().getTopSpecialDrawingTimes10();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5_special) {
                return mResult.getDrawingTime().getLastSpecialDrawingTimes10();
            }
        } else if (categoryType == R.string.analyze_fragment_result_recent_20) {
            if (contentType == R.string.analyze_fragment_result_item_top_5) {
                return mResult.getDrawingTime().getTopNormalDrawingTimes20();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5) {
                return mResult.getDrawingTime().getLastNormalDrawingTimes20();
            } else if (contentType == R.string.analyze_fragment_result_item_top_5_special) {
                return mResult.getDrawingTime().getTopSpecialDrawingTimes20();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5_special) {
                return mResult.getDrawingTime().getLastSpecialDrawingTimes20();
            }
        } else if (categoryType == R.string.analyze_fragment_result_this_month) {
            if (contentType == R.string.analyze_fragment_result_item_top_5) {
                return mResult.getDrawingTime().getTopNormalDrawingTimesThisMonth();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5) {
                return mResult.getDrawingTime().getLastNormalDrawingTimesThisMonth();
            } else if (contentType == R.string.analyze_fragment_result_item_top_5_special) {
                return mResult.getDrawingTime().getTopSpecialDrawingTimesThisMonth();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5_special) {
                return mResult.getDrawingTime().getLastSpecialDrawingTimesThisMonth();
            }
        } else if (categoryType == R.string.analyze_fragment_result_last_month) {
            if (contentType == R.string.analyze_fragment_result_item_top_5) {
                return mResult.getDrawingTime().getTopNormalDrawingTimesLast1Month();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5) {
                return mResult.getDrawingTime().getLastNormalDrawingTimesLast1Month();
            } else if (contentType == R.string.analyze_fragment_result_item_top_5_special) {
                return mResult.getDrawingTime().getTopSpecialDrawingTimesLast1Month();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5_special) {
                return mResult.getDrawingTime().getLastSpecialDrawingTimesLast1Month();
            }
        } else if (categoryType == R.string.analyze_fragment_result_last_2_month) {
            if (contentType == R.string.analyze_fragment_result_item_top_5) {
                return mResult.getDrawingTime().getTopNormalDrawingTimesLast2Month();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5) {
                return mResult.getDrawingTime().getLastNormalDrawingTimesLast2Month();
            } else if (contentType == R.string.analyze_fragment_result_item_top_5_special) {
                return mResult.getDrawingTime().getTopSpecialDrawingTimesLast2Month();
            } else if (contentType == R.string.analyze_fragment_result_item_last_5_special) {
                return mResult.getDrawingTime().getLastSpecialDrawingTimesLast2Month();
            }
        }
        throw new RuntimeException("unexpected type");
    }

    private String getTypeContentSummary(int categoryType, int contentType) {
        StringBuilder rtn = new StringBuilder();
        rtn.append(getFiveResultsFromList(getTypeContentDataByType(categoryType, contentType)));
        return rtn.toString();
    }

    private void bindTypeContent(RecyclerView.ViewHolder holder, int position) {
        JSONObject json = getItem(position);
        try {
            final int categoryType = json.getInt(KEY_CATEGORY);
            final int contentType = json.getInt(KEY_CONTENT_TYPE);

            final String title = json.getString(KEY_TITLE);
            final String summary = getTypeContentSummary(categoryType, contentType);

            AnalyzeResultContentHolder analyzeResultContentHolder = ((AnalyzeResultContentHolder) holder);
            analyzeResultContentHolder.getTitle().setText(title);
            analyzeResultContentHolder.getSummary().setText(summary);
            analyzeResultContentHolder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Pair<Integer, Integer>> rawData = getTypeContentDataByType(categoryType, contentType);
                    CharSequence[] data = new CharSequence[rawData.size()];
                    for (int i = 0; i < rawData.size(); ++i) {
                        data[i] = (String.format("%02d", rawData.get(i).first) + ": " + rawData.get(i).second);
                    }
                    new AlertDialog.Builder(mContext).setTitle(R.string.analyze_fragment_result_item_dialog_title)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setItems(data, null).create().show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindTypeCategory(RecyclerView.ViewHolder holder, int position) {
        JSONObject json = getItem(position);
        String category = "";
        try {
            category = json.getString(KEY_CATEGORY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((AnalyzeResultCategoryHolder) holder).getCategory().setText(category);
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return mItems.get(position).getInt(KEY_TYPE);
        } catch (JSONException e) {
            throw new RuntimeException("missing key type");
        }
    }

    public JSONObject getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
