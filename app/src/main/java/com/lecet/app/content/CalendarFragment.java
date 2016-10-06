package com.lecet.app.content;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.R;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements FlexibleCalendarView.OnDateClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public CalendarFragment() {
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        FlexibleCalendarView calendarView = (FlexibleCalendarView) view.findViewById(R.id.calendar_view);
        TextView tvMonth = (TextView) view.findViewById(R.id.tvMonth);

        calendarView.setOnDateClickListener(this);

        Calendar cal = Calendar.getInstance();
        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        tvMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, @BaseCellView.CellType int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_date_cell_view, null, false);
                    int size = getResources().getDimensionPixelSize(R.dimen.calendar_cell_size);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);
                    cellView.setLayoutParams(params);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_date_cell_view, null, false);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
                    cellView.setLayoutParams(params);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return "";
            }
        });

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {

                if (day % 7 == 0) {
                    List<EventW> eventList = new ArrayList<>();
                    eventList.add(new EventW());
                    return eventList;
                }
                return null;
            }
        });
        return view;
    }

    private void setupBinding() {
//        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        CalendarViewModel viewModel = new CalendarViewModel(this);
//        binding.setViewModel(viewModel);
    }

    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Toast.makeText(getActivity(), cal.getTime().toString() + " Clicked", Toast.LENGTH_SHORT).show();
    }

    public static class EventW implements Event {
        public EventW() {

        }

        @Override
        public int getColor() {
            return 0;
        }
    }

}
