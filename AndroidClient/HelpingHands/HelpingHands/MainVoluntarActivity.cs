using Android.App;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using AndroidX.AppCompat.App;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
using HelpingHands.Data;
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
        ListView EvenimenteListView;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main_voluntar);

            HomeView = FindViewById<GridLayout>(Resource.Id.HomeView);
            EvenimenteListView = FindViewById<ListView>(Resource.Id.EvenimenteListView);

            BottomNavigationView navigation = FindViewById<BottomNavigationView>(Resource.Id.navigation);
            navigation.SetOnNavigationItemSelectedListener(this);
        }
        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        List<Eveniment> Evenimente = new List<Eveniment>();

        async void LoadHome()
        {
            HomeView.Visibility = ViewStates.Visible;
            Evenimente = (await API.Client.GetEvenimente()).ToList();
            RunOnUiThread(() =>
            {
                EvenimenteListView.Adapter = new EvenimentAdapter(this, Evenimente);
            });
        }

        public bool OnNavigationItemSelected(IMenuItem item)
        {
            switch (item.ItemId)
            {
                case Resource.Id.navigation_home:
                    Task.Run(LoadHome);
                    return true;
                case Resource.Id.navigation_dashboard:
                    
                    return true;
                case Resource.Id.navigation_notifications:
                    
                    return true;
            }
            return false;
        }
    }
}

