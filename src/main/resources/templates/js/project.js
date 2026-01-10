// 项目同步功能
    document.getElementById('syncBtn').addEventListener('click', function() {
        const projectId = document.getElementById('projectId').value.trim();
        const syncBtn = this;
        const originalHTML = syncBtn.innerHTML;

        if (!projectId) {
            showError('请输入项目ID');
            return;
        }

        // 显示加载状态
        syncBtn.disabled = true;
        syncBtn.innerHTML = '<i class="bi bi-arrow-repeat spinner"></i> 同步中...';

        // 隐藏之前的结果
        hideResults();

        // 调用同步接口
        fetch('/api/project/sync?projectId=' + encodeURIComponent(projectId))
            .then(handleResponse)
            .then(handleSuccess)
            .catch(handleError)
            .finally(() => {
                syncBtn.disabled = false;
                syncBtn.innerHTML = originalHTML;
            });
    });

    // 获取URL列表功能
    document.getElementById('loadUrlsBtn').addEventListener('click', function() {
        const projectId = document.getElementById('testProjectId').value.trim();
        const loadBtn = this;
        const originalHTML = loadBtn.innerHTML;

        if (!projectId) {
            alert('请输入项目ID');
            return;
        }

        // 显示加载状态
        loadBtn.disabled = true;
        loadBtn.innerHTML = '<i class="bi bi-arrow-repeat spinner"></i> 加载中...';

        // 调用独立的URL列表接口
        fetch('/api/project/urls?projectId=' + encodeURIComponent(projectId))
            .then(handleResponse)
            .then(data => {
                if (data.success) {
                    populateUrlSelect(data.data.apiUrls || []);
                } else {
                    throw new Error(data.message || '获取URL列表失败');
                }
            })
            .catch(error => {
                alert('获取URL列表失败: ' + error.message);
                populateUrlSelect([]);
            })
            .finally(() => {
                loadBtn.disabled = false;
                loadBtn.innerHTML = originalHTML;
            });
    });

    function handleResponse(response) {
        if (!response.ok) {
            throw new Error('网络请求失败: ' + response.status);
        }
        return response.json();
    }

    function handleSuccess(data) {
        if (data.success) {
            document.getElementById('projectName').textContent = data.data.projectName || '未知项目';
            document.getElementById('apiCount').textContent = (data.data.apiCount || 0) + ' 个';
            document.getElementById('syncTime').textContent = new Date().toLocaleString();
            document.getElementById('syncResult').style.display = 'block';

            // 设置配置链接
            const projectId = document.getElementById('projectId').value.trim();
            const configLink = document.getElementById('configLink');
            configLink.href = `/project-config?projectId=${projectId}&projectName=${encodeURIComponent(data.data.projectName || '未知项目')}`;

        } else {
            throw new Error(data.message || '同步失败');
        }
    }

    function handleError(error) {
        showError(error.message);
    }

    function showError(message) {
        document.getElementById('errorMessage').textContent = message;
        document.getElementById('syncError').style.display = 'block';
    }

    function hideResults() {
        document.getElementById('syncResult').style.display = 'none';
        document.getElementById('syncError').style.display = 'none';
    }

    // 填充URL下拉框
    function populateUrlSelect(apiUrls) {
        const select = document.getElementById('apiUrlSelect');
        select.innerHTML = '<option value="">请选择接口URL</option>';

        if (apiUrls && apiUrls.length > 0) {
            apiUrls.forEach(url => {
                const option = document.createElement('option');
                option.value = url;
                option.textContent = url;
                select.appendChild(option);
            });
            // 自动选择第一个选项
            if (apiUrls.length > 0) {
                select.value = apiUrls[0];
            }
        } else {
            const option = document.createElement('option');
            option.value = "";
            option.textContent = "该项目暂无可用接口";
            select.appendChild(option);
        }
    }

    // 表单提交处理
    document.getElementById('testForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const selectedUrl = document.getElementById('apiUrlSelect').value;
        const customUrl = document.getElementById('customApiUrl').value.trim();

        // 验证至少有一个URL被填写
        if (!selectedUrl && !customUrl) {
            alert('请选择接口URL或输入自定义URL');
            return;
        }

        // 确定最终使用的URL
        const finalUrl = selectedUrl || customUrl;

        // 创建隐藏字段或直接设置表单action
        const urlInput = document.createElement('input');
        urlInput.type = 'hidden';
        urlInput.name = 'apiUrl';
        urlInput.value = finalUrl;
        this.appendChild(urlInput);

        // 显示加载状态
        const submitBtn = this.querySelector('button[type="submit"]');
        const spinner = document.getElementById('loadingSpinner');
        const submitText = document.getElementById('submitText');

        submitBtn.disabled = true;
        spinner.style.display = 'inline-block';
        submitText.textContent = ' 测试中...';

        // 提交表单
        this.submit();
    });

    // 回车键触发同步
    document.getElementById('projectId').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            document.getElementById('syncBtn').click();
        }
    });

    // 测试项目ID回车触发获取URL
    document.getElementById('testProjectId').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            document.getElementById('loadUrlsBtn').click();
        }
    });