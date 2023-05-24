﻿using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.SE.Omapi;
using Android.Views;
using Android.Widget;
using AndroidX.AppCompat.App;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Data;
using HelpingHands.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xamarin.Essentials;

namespace HelpingHands
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    public class MainVoluntarActivity : AppCompatActivity, BottomNavigationView.IOnNavigationItemSelectedListener
    {
        GridLayout HomeView;
        GridLayout DashboardView;
        ListView EvenimenteListView;
        ListView OrganizatorEvenimenteListView;
        Button EvNextButton;
        Button EvPrevButton;
        TextView EvPageTextView;
        Button CreateEvButton;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main_voluntar);

            HomeView = FindViewById<GridLayout>(Resource.Id.HomeView);            
            EvenimenteListView = FindViewById<ListView>(Resource.Id.EvenimenteListView);
            EvNextButton = FindViewById<Button>(Resource.Id.EvNextButton);
            EvPrevButton = FindViewById<Button>(Resource.Id.EvPrevButton);            
            EvPageTextView = FindViewById<TextView>(Resource.Id.EvPageTextView);

            EvPrevButton.Click += EvPrevButton_Click;
            EvNextButton.Click += EvNextButton_Click;

            DashboardView = FindViewById<GridLayout>(Resource.Id.DashboardView);
            OrganizatorEvenimenteListView = FindViewById<ListView>(Resource.Id.OrganizatorEvenimenteListView);
            CreateEvButton = FindViewById<Button>(Resource.Id.CreateEvButton);
            CreateEvButton.Click += CreateEvButton_Click;

            EvenimenteListView.ItemClick += EvenimenteListView_ItemClick;
            EvenimenteListView.ItemSelected += EvenimenteListView_ItemSelected;

            BottomNavigationView navigation = FindViewById<BottomNavigationView>(Resource.Id.navigation);
            navigation.SetOnNavigationItemSelectedListener(this);


            HomeView.Visibility = ViewStates.Visible;
            DashboardView.Visibility = ViewStates.Gone;
            Task.Run(LoadHome);
            Task.Run(LoadDashboard);
        }

        private void EvenimenteListView_ItemClick(object sender, AdapterView.ItemClickEventArgs e)
        {
            Console.WriteLine($"Clicked index {e.Position}");
            var ev = (EvenimenteListView.Adapter as EvenimentAdapter)[e.Position];
            Console.WriteLine($"{ev}");

            Intent intent = new Intent(this, typeof(ViewEvenimentVoluntarActivity));
            intent.PutExtra("eveniment", JsonConvert.SerializeObject(ev));
            StartActivity(intent);

        }

        private void EvenimenteListView_ItemSelected(object sender, AdapterView.ItemSelectedEventArgs e)
        {
            Console.WriteLine("Selected index ");
            Intent intent = new Intent(this, typeof(ViewEvenimentVoluntarActivity));            

            var ev = (EvenimenteListView.Adapter as EvenimentAdapter)[EvenimenteListView.SelectedItemPosition];

            Console.WriteLine(ev);
            intent.PutExtra("eveniment", JsonConvert.SerializeObject(ev));
            StartActivity(intent);
        }

        void CreateEvButton_Click(object sender, EventArgs e)
        {
            Intent intent = new Intent(this, typeof(AddEvenimentActivity));
            StartActivity(intent);
        }

        async void EvPrevButton_Click(object sender, EventArgs e)
        {
            try
            {
                var pageN = int.Parse(EvPageTextView.Text);
                if (pageN == 1) return;
                pageN--;
                Evenimente = (await API.Client.GetEvenimentePaged(pageN - 1)).ToList();
                RunOnUiThread(() =>
                {
                    EvPageTextView.Text = pageN.ToString();
                    EvenimenteListView.Adapter = new EvenimentAdapter(this, Evenimente);
                });
            }
            catch (Exception ex)
            {
                await MessageBox.Alert(this, "GET request failed. Connection possibly failed", "Error");
            }
        }

        async void EvNextButton_Click(object sender, EventArgs e)
        {
            try
            {
                var pageN = int.Parse(EvPageTextView.Text);
                pageN++;
                Evenimente = (await API.Client.GetEvenimentePaged(pageN - 1)).ToList();
                RunOnUiThread(() =>
                {
                    EvPageTextView.Text = pageN.ToString();
                    EvenimenteListView.Adapter = new EvenimentAdapter(this, Evenimente);
                });
            }
            catch (Exception ex)
            {
                await MessageBox.Alert(this, "GET request failed. Connection possibly failed", "Error");
            }
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        List<Eveniment> Evenimente = new List<Eveniment>();
        List<Eveniment> ManagedEvenimente = new List<Eveniment>();

        async void LoadHome()
        {
            try
            {
                Evenimente = (await API.Client.GetEvenimentePaged(int.Parse(EvPageTextView.Text) - 1)).ToList();
                RunOnUiThread(() =>
                {
                    EvenimenteListView.Adapter = new EvenimentAdapter(this, Evenimente);
                });
            }
            catch(Exception e)
            {
                await MessageBox.Alert(this, "GET request failed. Connection possibly failed", "Error");
            }
        }

        async void LoadDashboard()
        {
            try
            {
                ManagedEvenimente = (await API.Client.GetEvenimenteByOrganizerId(AppSession.UserId)).ToList();
                RunOnUiThread(() =>
                {
                    OrganizatorEvenimenteListView.Adapter = new EvenimentAdapter(this, ManagedEvenimente);
                });
            }
            catch (Exception e)
            {
                await MessageBox.Alert(this, "GET request failed. Connection possibly failed", "Error");
            }

        }

        public bool OnNavigationItemSelected(IMenuItem item)
        {
            switch (item.ItemId)
            {
                case Resource.Id.navigation_home:
                    HomeView.Visibility = ViewStates.Visible;
                    DashboardView.Visibility = ViewStates.Gone;
                    LoadHome();
                    return true;
                case Resource.Id.navigation_dashboard:
                    HomeView.Visibility = ViewStates.Gone;
                    DashboardView.Visibility = ViewStates.Visible;
                    LoadDashboard();
                    return true;
                case Resource.Id.navigation_notifications:
                    
                    return true;
            }
            return false;
        }
    }
}

