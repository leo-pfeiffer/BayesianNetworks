# EVALUATION
# Running this file will grab the JSON file containing the experiment results and create the
# plots and tables in the evaluation/out folder.

# built-in imports
import json
import os

# make sure pandas and matplotlib are installed
try:
    import pandas as pd
    from matplotlib import pyplot as plt
    from matplotlib.ticker import MultipleLocator
    from matplotlib import cycler
    import seaborn as sns
except ImportError:
    print("ImportError: Missing dependencies. Please install the following python packages:")
    print("pandas, matplotlib, seaborn")
    exit(1)


# file and path constants
OUT_FOLDER = 'evaluation/out/'
D_FILE = 'diagnostic-results.json'
P_FILE = 'predictive-results.json'

def load(path):
    with open(path, 'r') as f:
        data = json.load(f)
    return data

def filter_df(df, column, values):
    return df[df[column].isin(values)]

def set_plt_params():

    major = 5.0
    minor = 3.0

    plt.style.use('default')
    plt.rcParams['font.size'] = 15
    plt.rcParams['legend.fontsize'] = 18
    plt.rcParams['text.usetex'] = True
    plt.rcParams['xtick.minor.size'] = minor
    plt.rcParams['xtick.major.size'] = major
    plt.rcParams['ytick.minor.size'] = minor
    plt.rcParams['ytick.major.size'] = major
    plt.rcParams['xtick.direction'] = 'in'
    plt.rcParams['ytick.direction'] = 'in'

def create_plot(df: pd.DataFrame, file_name: str, title: str, ycol: str):

    xsize = 15
    ysize = 5

    fig, ax = plt.subplots(figsize=(xsize, ysize))

    ax = df.plot.bar(rot=0, figsize=(xsize, ysize))

    plt.legend(loc='best')
    plt.title(title)
    plt.ylabel(f'${ycol}$', labelpad = 10)
    plt.xticks(rotation = 90)
    plt.savefig(f"{OUT_FOLDER}{file_name}.png", dpi=300, pad_inches=.15, bbox_inches = 'tight')

def boxplot(x, y, data, hue, file_name, title):
    xsize = 15
    ysize = 5

    fig, ax = plt.subplots(figsize=(xsize, ysize))

    ax = sns.boxplot(x=x, y=y, data=data, hue=hue, fliersize=3)
    plt.title(title)
    plt.ylabel(f'${y}$', labelpad = 10)
    plt.xticks(rotation = 90)
    plt.savefig(f"{OUT_FOLDER}{file_name}.png", dpi=300, pad_inches=.15, bbox_inches = 'tight')

if __name__ == '__main__':
    set_plt_params()
    d_data = load(OUT_FOLDER + D_FILE)
    p_data = load(OUT_FOLDER + P_FILE)
    d_df = pd.DataFrame(d_data)
    p_df = pd.DataFrame(p_data)

    boxplot(x='network',y='runtime', data=d_df, hue='order',
        file_name='diagnostic-runtime', title="Diagnostic Queries: Runtime (ns)")
    boxplot(x='network',y='runtime', data=p_df, hue='order',
        file_name='predictive-runtime', title="Predictive Queries: Runtime (ns)")

    boxplot(x='network',y='maxFactorSize', data=d_df, hue='order',
        file_name='diagnostic-maxfactor', title="Diagnostic Queries: Max Factor Size")
    boxplot(x='network',y='maxFactorSize', data=p_df, hue='order',
        file_name='predictive-maxfactor', title="Predictive Queries: Max Factor Size")
