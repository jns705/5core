# -*- coding: utf-8 -*-
import streamlit as st
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
import os

# CSS
st.markdown("""
<style>
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
    .main .block-container { padding-top: 1.5rem; font-family: 'Inter', sans-serif; }
    .header-section { background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%); padding: 3rem 2rem; border-radius: 20px; margin-bottom: 2.5rem; border: 1px solid #e2e8f0; box-shadow: 0 4px 20px rgba(0,0,0,0.05); }
    .header-title { font-size: 2.8rem; font-weight: 700; color: #1e293b; margin: 0 0 0.5rem 0; letter-spacing: -0.02em; }
    .header-subtitle { font-size: 1.2rem; color: #64748b; font-weight: 400; margin: 0; }
    .kpi-card { background: white; padding: 2rem; border-radius: 16px; text-align: center; box-shadow: 0 4px 20px rgba(0,0,0,0.08); border: 1px solid #f1f5f9; transition: all 0.2s ease; }
    .kpi-card:hover { box-shadow: 0 8px 32px rgba(0,0,0,0.12); transform: translateY(-2px); }
    .kpi-label { color: #64748b; font-size: 14px; font-weight: 500; margin-bottom: 0.75rem; letter-spacing: 0.025em; }
    .kpi-value { color: #1e293b; font-size: 36px; font-weight: 700; line-height: 1; }
    .kpi-unit { color: #94a3b8; font-size: 13px; font-weight: 500; margin-top: 0.25rem; }
    .chart-card { background: white; padding: 2rem; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); border: 1px solid #f1f5f9; }
    .chart-title { font-size: 1.3rem; font-weight: 600; color: #1e293b; margin: 0 0 1.5rem 0; padding-bottom: 0.75rem; border-bottom: 2px solid #e2e8f0; }
    .year-btn { height: 60px !important; border-radius: 12px !important; font-size: 16px !important; font-weight: 600 !important; border: 2px solid #e2e8f0; transition: all 0.2s ease; font-family: 'Inter', sans-serif; }
    .year-btn[data-baseweb-button-primary] { background: #1e40af !important; border-color: #1e40af !important; color: white !important; box-shadow: 0 4px 12px rgba(30,64,175,0.3); }
    .comparison-card { background: linear-gradient(90deg, #f1f5f9 0%, #e2e8f0 100%); padding: 1.5rem; border-radius: 12px; border-left: 5px solid #10b981; }
    .subtotal-row { background: linear-gradient(90deg, #f8fafc, #e2e8f0) !important; font-weight: 700 !important; border-top: 3px solid #1e40af !important; font-size: 14px !important; }
    .growth-positive { color: #059669; font-weight: 600; }
    .growth-negative { color: #dc2626; font-weight: 600; }
</style>
""", unsafe_allow_html=True)

st.set_page_config(page_title="HMC 판매 분석", layout="wide", initial_sidebar_state="collapsed")

# 헤더
st.markdown("""
<div class="header-section">
    <h1 class="header-title">현대차 판매 실적 분석</h1>
    <p class="header-subtitle">모델별 판매량 및 월별 추이 전문 분석 리포트 (2024 ↔ 2025 비교)</p>
</div>
""", unsafe_allow_html=True)

# 데이터 전처리 함수
def filter_data(df):
    if df.empty: return df
    exclude_keywords = ['sub-total', 'sub total', '합계', 'total']
    return df[~df['Model'].str.contains('|'.join(exclude_keywords), case=False, na=False)]

def get_subtotal_row(df):
    subtotal_rows = df[df['Model'].str.contains('sub-total|sub total', case=False, na=False)]
    return subtotal_rows.iloc[0] if not subtotal_rows.empty else None

# 상태 초기화
if "selected_year" not in st.session_state:
    st.session_state.selected_year = 2024
if "show_comparison" not in st.session_state:
    st.session_state.show_comparison = False

# 데이터 로드 함수
@st.cache_data(ttl=300)
def load_raw_data(year):
    try:
        file_map = {2024: "HMC-modelbyeol-panmae-2024nyeon-clean.xlsx", 2025: "HMC-modelbyeol-panmae-2025nyeon-clean.xlsx"}
        file = file_map.get(year)
        if os.path.exists(file):
            return pd.read_excel(file)
        return pd.DataFrame()
    except:
        return pd.DataFrame()

# 연도 선택 + 비교 토글
col1, col2, col3 = st.columns([1, 1, 0.8], gap="large")
if col1.button("2024년" if st.session_state.selected_year != 2024 else "2024년 (선택됨)", use_container_width=True, key="btn_2024"):
    st.session_state.selected_year = 2024; st.rerun()
if col2.button("2025년" if st.session_state.selected_year != 2025 else "2025년 (선택됨)", use_container_width=True, key="btn_2025"):
    st.session_state.selected_year = 2025; st.rerun()
if col3.button("🔄 비교" if not st.session_state.show_comparison else "📊 단일", use_container_width=True, key="toggle_comparison"):
    st.session_state.show_comparison = not st.session_state.show_comparison; st.rerun()

selected_year = st.session_state.selected_year
show_comparison = st.session_state.show_comparison

# 선택 정보 표시
st.markdown(f"""
<div style='background: #f8fafc; padding: 1rem 1.5rem; border-radius: 12px; border-left: 4px solid #1e40af; margin-bottom: 2rem;'>
    <strong style='color: #1e293b; font-size: 15px;'>분석 대상: <span style='color: #1e40af;'>{selected_year}년</span> 
    <span style='color: #10b981; font-weight: 500;'>{" | 비교모드 활성화" if show_comparison else ""}</span></strong>
</div>
""", unsafe_allow_html=True)

# 데이터 로드
df_raw_current = load_raw_data(selected_year)
df_current = filter_data(df_raw_current)
subtotal_current = get_subtotal_row(df_raw_current)

df_raw_compare = None
df_compare = None
subtotal_compare = None
compare_year = None

if show_comparison:
    compare_year = 2024 if selected_year == 2025 else 2025
    df_raw_compare = load_raw_data(compare_year)
    df_compare = filter_data(df_raw_compare)
    subtotal_compare = get_subtotal_row(df_raw_compare)

if df_current.empty:
    st.error("데이터 로드 실패"); st.stop()

total_current = int(df_current['Total'].sum())

# KPI (2열로 수정)
col1, col2 = st.columns(2, gap="large")
with col1:
    st.markdown(f"""
    <div class="kpi-card">
        <div class="kpi-label">총 판매대수 ({selected_year})</div>
        <div class="kpi-value">{total_current:,}</div>
        <div class="kpi-unit">대</div>
    </div>
    """, unsafe_allow_html=True)

if show_comparison and df_compare is not None:
    total_compare = int(df_compare['Total'].sum())
    growth_rate = ((total_current - total_compare) / total_compare * 100)
    with col2:
        st.markdown(f"""
        <div class="kpi-card">
            <div class="kpi-label">총 판매대수 ({compare_year})</div>
            <div class="kpi-value">{total_compare:,}</div>
            <div class="kpi-unit">
                <span class="{'growth-positive' if growth_rate > 0 else 'growth-negative'}">{growth_rate:+.1f}%</span>
            </div>
        </div>
        """, unsafe_allow_html=True)
else:
    with col2:
        st.markdown(f"""
        <div class="kpi-card">
            <div class="kpi-label">등록 모델수</div>
            <div class="kpi-value">{len(df_current):,}</div>
            <div class="kpi-unit">종</div>
        </div>
        """, unsafe_allow_html=True)

st.markdown("---")

if show_comparison and df_compare is not None:
    # 월별 비교
    st.markdown('<div class="comparison-card">', unsafe_allow_html=True)
    st.markdown('<h3 style="color: #059669; margin-bottom: 1.5rem;">월별 판매량 비교</h3>', unsafe_allow_html=True)
    
    months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    monthly_current = df_current[months].sum()
    monthly_compare = df_compare[months].sum()
    
    fig_monthly = go.Figure()
    fig_monthly.add_trace(go.Bar(name=f'{selected_year}', x=months, y=monthly_current.values, marker_color='#1e40af'))
    fig_monthly.add_trace(go.Bar(name=f'{compare_year}', x=months, y=monthly_compare.values, marker_color='#94a3b8'))
    fig_monthly.update_layout(barmode='group', height=400, title="월별 판매량 비교", xaxis_title="월", yaxis_title="판매대수", showlegend=True, yaxis_tickformat=',')
    st.plotly_chart(fig_monthly, use_container_width=True)
    st.markdown('</div>', unsafe_allow_html=True)

    # 모델별 성장률
    st.markdown('<div class="comparison-card">', unsafe_allow_html=True)
    st.markdown('<h3 style="color: #059669; margin-bottom: 1.5rem;">모델별 성장률 TOP 15</h3>', unsafe_allow_html=True)
    
    common_models = set(df_current['Model']).intersection(set(df_compare['Model']))
    growth_data = []
    for model in common_models:
        curr_val = df_current[df_current['Model'] == model]['Total'].iloc[0]
        comp_val = df_compare[df_compare['Model'] == model]['Total'].iloc[0]
        growth_rate = ((curr_val - comp_val) / comp_val * 100) if comp_val > 0 else 0
        growth_data.append({'Model': model, f'{selected_year}': curr_val, '성장률': growth_rate})
    
    growth_df = pd.DataFrame(growth_data).sort_values('성장률', ascending=False).head(15)
    fig_growth = px.bar(growth_df, x='성장률', y='Model', orientation='h', 
                       color='성장률', color_continuous_scale=['#ef4444', '#f59e0b', '#10b981'])
    fig_growth.update_layout(height=450, title="모델별 성장률", xaxis_title="성장률 (%)")
    st.plotly_chart(fig_growth, use_container_width=True)
    st.markdown('</div>', unsafe_allow_html=True)

st.markdown("---")

# 기존 단일 분석 차트들
col1, col2 = st.columns(2, gap="large")
with col1:
    st.markdown('<div class="chart-card">', unsafe_allow_html=True)
    st.markdown('<h3 class="chart-title">TOP 10 판매모델</h3>', unsafe_allow_html=True)
    top10 = df_current.nlargest(10, 'Total')[['Model', 'Total']].sort_values('Total')
    fig_bar = px.bar(top10, x='Total', y='Model', orientation='h', color='Total', 
                     color_continuous_scale=['#e2e8f0', '#1e40af'], labels={'Total': '판매대수'})
    fig_bar.update_layout(height=420, margin=dict(l=160), showlegend=False, xaxis_tickformat=',')
    st.plotly_chart(fig_bar, use_container_width=True)
    st.markdown('</div>', unsafe_allow_html=True)

with col2:
    st.markdown('<div class="chart-card">', unsafe_allow_html=True)
    st.markdown('<h3 class="chart-title">판매 비중 (TOP 15)</h3>', unsafe_allow_html=True)
    top15 = df_current.head(15)
    fig_pie = px.pie(top15, values='Total', names='Model', hole=0.4)
    fig_pie.update_traces(textposition='inside', textinfo='percent+label')
    fig_pie.update_layout(height=420, showlegend=False)
    st.plotly_chart(fig_pie, use_container_width=True)
    st.markdown('</div>', unsafe_allow_html=True)

# 월별 추이
st.markdown('<div class="chart-card">', unsafe_allow_html=True)
st.markdown('<h3 class="chart-title">월별 판매 추이</h3>', unsafe_allow_html=True)
months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
monthly_total = df_current[months].sum()
fig_line = px.line(x=months, y=monthly_total.values, markers=True, line_shape='spline', color_discrete_sequence=['#1e40af'])
fig_line.update_layout(height=420, yaxis_title="판매대수", yaxis_tickformat=',', showlegend=False)
st.plotly_chart(fig_line, use_container_width=True)
st.markdown('</div>', unsafe_allow_html=True)

# 상세 테이블
st.markdown('<div class="chart-card">', unsafe_allow_html=True)
st.markdown(f'<h3 class="chart-title">{selected_year}년 모델별 상세 판매 데이터</h3>', unsafe_allow_html=True)

display_data = []
if subtotal_current is not None:
    subtotal_display = {col: f"{int(subtotal_current[col]):,}" if col != 'Model' else subtotal_current['Model'] for col in ['Model'] + ['Total'] + months}
    display_data.append(subtotal_display)

for _, row in df_current.iterrows():
    row_display = {col: f"{int(row[col]):,}" if col != 'Model' else row['Model'] for col in ['Model', 'Total'] + months}
    display_data.append(row_display)

display_df = pd.DataFrame(display_data)
st.dataframe(display_df, use_container_width=True, hide_index=True)
st.markdown('</div>', unsafe_allow_html=True)
