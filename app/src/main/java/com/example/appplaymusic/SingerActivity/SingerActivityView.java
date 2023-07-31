package com.example.appplaymusic.SingerActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.Category;
import com.example.appplaymusic.CategoryNgheSiAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.R;

import java.util.ArrayList;
import java.util.List;

public interface SingerActivityView  {

    void receiveSinger(Singer body);
}